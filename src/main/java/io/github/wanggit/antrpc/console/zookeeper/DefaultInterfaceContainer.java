package io.github.wanggit.antrpc.console.zookeeper;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.stream.Collectors;

@Component
@Slf4j
public class DefaultInterfaceContainer implements IInterfaceContainer {

    private final Set<InterfaceDTO> interfaceSet = new CopyOnWriteArraySet<>();
    private final Set<InterfaceMethodDTO> interfaceMethodSet = new CopyOnWriteArraySet<>();

    @Override
    public void addInterface(InterfaceNodeDataBean interfaceNodeDataBean, String path) {
        InterfaceDTO interfaceDTO = new InterfaceDTO();
        interfaceDTO.setClassName(interfaceNodeDataBean.getClassName());
        interfaceDTO.setHost(interfaceNodeDataBean.getHost());
        interfaceDTO.setPath(path);
        interfaceDTO.setRegisterTs(interfaceNodeDataBean.getTs());
        if (log.isDebugEnabled()) {
            log.debug("save to local --> " + JSONObject.toJSONString(interfaceDTO));
        }
        Map<String, RegisterBeanMethod> methodMap = interfaceNodeDataBean.getMethodMap();
        for (Map.Entry<String, RegisterBeanMethod> entry : methodMap.entrySet()) {
            InterfaceMethodDTO interfaceMethodDTO = new InterfaceMethodDTO();
            interfaceMethodDTO.setClassName(interfaceDTO.getClassName());
            RegisterBeanMethod registerBeanMethod = entry.getValue();
            interfaceMethodDTO.setDurationInSeconds(registerBeanMethod.getDurationInSeconds());
            interfaceMethodDTO.setLimit(registerBeanMethod.getLimit());
            interfaceMethodDTO.setMethodName(registerBeanMethod.getMethodName());
            interfaceMethodDTO.setParameterTypeNames(registerBeanMethod.getParameterTypeNames());
            if (log.isDebugEnabled()) {
                log.debug("save to local --> " + interfaceMethodDTO.toString());
            }
            interfaceMethodSet.add(interfaceMethodDTO);
            interfaceDTO.addInterfaceMethodDTO(interfaceMethodDTO);
        }
        interfaceSet.add(interfaceDTO);
    }

    @Override
    public void updateInterface(InterfaceNodeDataBean interfaceNodeDataBean, String path) {
        if (log.isDebugEnabled()) {
            log.debug("update " + path);
        }
        deleteInterface(interfaceNodeDataBean, path);
        addInterface(interfaceNodeDataBean, path);
    }

    @Override
    public void deleteInterface(InterfaceNodeDataBean interfaceNodeDataBean, String path) {
        if (log.isDebugEnabled()) {
            log.debug("delete " + path);
        }
        List<InterfaceDTO> dtos =
                interfaceSet.stream()
                        .filter(it -> Objects.equals(it.getPath(), path))
                        .collect(Collectors.toList());
        for (InterfaceDTO dto : dtos) {
            interfaceMethodSet.removeAll(dto.getInterfaceMethodDTOS());
        }
        interfaceSet.removeAll(dtos);
    }

    @Override
    public List<InterfaceDTO> findByInterfaceName(String interfaceName) {
        return interfaceSet.stream()
                .filter(it -> it.getClassName().toLowerCase().contains(interfaceName.toLowerCase()))
                .sorted((o1, o2) -> o1.getClassName().compareToIgnoreCase(o2.getClassName()))
                .collect(Collectors.toList());
    }

    @Override
    public List<InterfaceMethodDTO> findByInterfaceMethodName(String methodName) {
        return interfaceMethodSet.stream()
                .filter(it -> it.getMethodName().toLowerCase().contains(methodName.toLowerCase()))
                .sorted((o1, o2) -> o1.getMethodName().compareToIgnoreCase(o2.getMethodName()))
                .collect(Collectors.toList());
    }
}
