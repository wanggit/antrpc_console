package io.github.wanggit.antrpc.console.zookeeper;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
class InterfaceNodeDataBean {
    private Long ts;
    private List<String> methods;
    private Map<String, RegisterBeanMethod> methodMap;
    private String className;
    private String host;
}
