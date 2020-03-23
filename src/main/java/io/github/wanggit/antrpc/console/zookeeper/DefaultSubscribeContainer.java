package io.github.wanggit.antrpc.console.zookeeper;

import io.github.wanggit.antrpc.console.web.vo.SubscribeNodeVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.*;

@Slf4j
@Component
public class DefaultSubscribeContainer implements ISubscribeContainer {

    private final Map<String, TreeSet<SubscribeNodeVO>> nodes = new HashMap<>();

    @Override
    public void addSubscribeNode(SubscribeNode subscribeNode) {
        if (null == subscribeNode) {
            throw new IllegalArgumentException("argument not be null.");
        }
        if (!nodes.containsKey(subscribeNode.getClassName())) {
            nodes.put(
                    subscribeNode.getClassName(),
                    new TreeSet<>((o1, o2) -> o1.getHost().compareToIgnoreCase(o2.getHost())));
        }
        SubscribeNodeVO subscribeNodeVO = new SubscribeNodeVO();
        subscribeNodeVO.setHost(subscribeNode.getHost());
        subscribeNodeVO.setTs(subscribeNode.getTs());
        nodes.get(subscribeNode.getClassName()).add(subscribeNodeVO);
    }

    @Override
    public void deleteSubscribeNode(SubscribeNode subscribeNode) {
        if (null == subscribeNode) {
            throw new IllegalArgumentException("argument not be null.");
        }
        if (nodes.containsKey(subscribeNode.getClassName())) {
            TreeSet<SubscribeNodeVO> treeSet = nodes.get(subscribeNode.getClassName());
            SubscribeNodeVO subscribeNodeVO = new SubscribeNodeVO();
            subscribeNodeVO.setHost(subscribeNode.getHost());
            subscribeNodeVO.setTs(subscribeNode.getTs());
            treeSet.remove(subscribeNodeVO);
            if (treeSet.isEmpty()) {
                nodes.remove(subscribeNode.getClassName());
            }
        }
    }

    @Override
    public List<SubscribeNodeVO> findByClassName(String className) {
        TreeSet<SubscribeNodeVO> subscribeNodeVOS = nodes.get(className);
        if (null == subscribeNodeVOS || subscribeNodeVOS.isEmpty()) {
            return new ArrayList<>();
        }
        return new ArrayList<>(subscribeNodeVOS);
    }
}
