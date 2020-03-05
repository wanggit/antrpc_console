package io.github.wanggit.antrpc.console.zookeeper;

import java.util.List;

public interface IInterfaceContainer {

    void addInterface(InterfaceNodeDataBean interfaceNodeDataBean, String path);

    void updateInterface(InterfaceNodeDataBean interfaceNodeDataBean, String path);

    void deleteInterface(InterfaceNodeDataBean interfaceNodeDataBean, String path);

    List<InterfaceDTO> findByInterfaceName(String interfaceName);

    List<InterfaceMethodDTO> findByInterfaceMethodName(String methodName);
}
