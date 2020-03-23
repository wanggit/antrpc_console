package io.github.wanggit.antrpc.console.invoker;

import io.github.wanggit.antrpc.commons.bean.*;
import io.github.wanggit.antrpc.commons.codec.cryption.ICodec;
import io.github.wanggit.antrpc.commons.codec.serialize.ISerializer;
import io.github.wanggit.antrpc.commons.constants.ConstantValues;
import io.github.wanggit.antrpc.commons.future.ReadClientFuture;
import io.github.wanggit.antrpc.console.ConsoleConstantValues;
import io.github.wanggit.antrpc.console.invoker.client.DefaultClient;
import io.github.wanggit.antrpc.console.invoker.client.IClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.ClassUtils;

import javax.annotation.PostConstruct;
import java.util.HashMap;

@Slf4j
@Component
public class DefaultServiceProviderInvoker implements IServiceProviderInvoker {

    @Value("${antrpc.service.codec.enable}")
    private boolean codecEnable;

    @Value("${antrpc.service.codec.type}")
    private String codecType;

    @Value("${antrpc.service.codec.key}")
    private String codecKey;

    @Value("${antrpc.serialize.type}")
    private String serializeType;

    private ICodec codec;

    private ISerializer serializer;

    @PostConstruct
    public void init()
            throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        if (codecEnable) {
            Class<?> codecClazz =
                    ClassUtils.forName(
                            codecType, DefaultServiceProviderInvoker.class.getClassLoader());
            codec = (ICodec) codecClazz.newInstance();
            codec.setKey(codecKey);
        }
        Class<?> serializeClazz =
                ClassUtils.forName(
                        serializeType, DefaultServiceProviderInvoker.class.getClassLoader());
        serializer = (ISerializer) serializeClazz.newInstance();
        serializer.setConfigs(new HashMap<>());
        serializer.init();
    }

    @Override
    public Object invoke(InvokeDTO invokeDTO) {
        RpcRequestBean requestBean = createBasicRpcRequestBean();
        requestBean.setArgumentTypes(invokeDTO.getParameterTypeNames());
        requestBean.setFullClassName(invokeDTO.getClassName());
        requestBean.setMethodName(invokeDTO.getMethodName());
        requestBean.setArgumentValues(invokeDTO.getArgumentValues());
        Host targetHost = invokeDTO.getHost();
        RpcProtocol protocol = new RpcProtocol();
        protocol.setCmdId(IdGenHelper.getInstance().getId());
        protocol.setType(ConstantValues.BIZ_TYPE);
        protocol.setData(serializer.serialize(requestBean));
        IClient client;
        try {
            client = new DefaultClient(targetHost, codec, serializer);
        } catch (InterruptedException e) {
            if (log.isErrorEnabled()) {
                log.error("create DefaultClient ERROR.", e);
            }
            throw new RuntimeException();
        }
        ReadClientFuture future = client.send(protocol);
        RpcResponseBean responseBean = future.get();
        return responseBean.getResult();
    }

    private RpcRequestBean createBasicRpcRequestBean() {
        RpcRequestBean rpcRequestBean = new RpcRequestBean();
        rpcRequestBean.setTs(System.currentTimeMillis());
        rpcRequestBean.setOneway(false);
        rpcRequestBean.setCaller(ConsoleConstantValues.CONSOLE_CALLER);
        rpcRequestBean.setId(IdGenHelper.getInstance().getUUID());
        rpcRequestBean.setSerialNumber(IdGenHelper.getInstance().getUUID());
        return rpcRequestBean;
    }
}
