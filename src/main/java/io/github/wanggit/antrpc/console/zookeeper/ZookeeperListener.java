package io.github.wanggit.antrpc.console.zookeeper;

import com.alibaba.fastjson.JSONObject;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.ChildData;
import org.apache.curator.framework.recipes.cache.TreeCache;
import org.apache.curator.framework.recipes.cache.TreeCacheEvent;
import org.apache.curator.framework.recipes.cache.TreeCacheListener;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.nio.charset.Charset;

@Component
public class ZookeeperListener {

    @Value("${antrpc.zookeeper}")
    private String hosts;

    private CuratorFramework curatorFramework;

    @Autowired private IInterfaceContainer interfaceContainer;

    @PostConstruct
    public void init() throws Exception {
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 5, 30000);
        curatorFramework =
                CuratorFrameworkFactory.builder()
                        .connectString(hosts)
                        .connectionTimeoutMs(5000)
                        .retryPolicy(retryPolicy)
                        .build();
        curatorFramework.start();
        TreeCache treeCache =
                new TreeCache(curatorFramework, "/" + ConstantValues.ZK_ROOT_NODE_NAME);
        treeCache
                .getListenable()
                .addListener(
                        new TreeCacheListener() {
                            @Override
                            public void childEvent(CuratorFramework client, TreeCacheEvent event)
                                    throws Exception {
                                if (TreeCacheEvent.Type.NODE_ADDED.equals(event.getType())
                                        || TreeCacheEvent.Type.NODE_UPDATED.equals(event.getType())
                                        || TreeCacheEvent.Type.NODE_REMOVED.equals(
                                                event.getType())) {
                                    doEventOnType(event);
                                }
                            }
                        });
        treeCache.start();
    }

    private void doEventOnType(TreeCacheEvent event) {
        ChildData childData = event.getData();
        ZkNodeType.Type type = ZkNodeType.getType(childData.getPath());
        if (ZkNodeType.Type.INTERFACE.equals(type)) {
            byte[] data = childData.getData();
            String json = new String(data, Charset.forName("UTF-8"));
            InterfaceNodeDataBean interfaceNodeDataBean =
                    JSONObject.parseObject(json, InterfaceNodeDataBean.class);
            fullClassInfo(interfaceNodeDataBean, childData.getPath());
            if (TreeCacheEvent.Type.NODE_ADDED.equals(event.getType())) {
                interfaceContainer.addInterface(interfaceNodeDataBean, childData.getPath());
            } else if (TreeCacheEvent.Type.NODE_UPDATED.equals(event.getType())) {
                interfaceContainer.updateInterface(interfaceNodeDataBean, childData.getPath());
            } else if (TreeCacheEvent.Type.NODE_REMOVED.equals(event.getType())) {
                interfaceContainer.deleteInterface(interfaceNodeDataBean, childData.getPath());
            }
        }
    }

    private void fullClassInfo(InterfaceNodeDataBean interfaceNodeDataBean, String path) {
        path = path.replaceFirst("/" + ConstantValues.ZK_ROOT_NODE_NAME + "/", "");
        String[] tmps = path.split("/");
        String host = tmps[0];
        String className = tmps[1];
        interfaceNodeDataBean.setClassName(className);
        interfaceNodeDataBean.setHost(host);
    }
}
