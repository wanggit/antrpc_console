package io.github.wanggit.antrpc.console.web;

import io.github.wanggit.antrpc.commons.generic.IServiceProviderInvoker;
import io.github.wanggit.antrpc.commons.generic.InvokeDTO;
import io.github.wanggit.antrpc.console.web.utils.InvokeDTOUtil;
import io.github.wanggit.antrpc.console.web.vo.Result;
import io.github.wanggit.antrpc.console.zookeeper.IInterfaceContainer;
import io.github.wanggit.antrpc.console.zookeeper.ISubscribeContainer;
import io.github.wanggit.antrpc.console.zookeeper.InterfaceDTO;
import io.github.wanggit.antrpc.console.zookeeper.InterfaceMethodDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("interface")
public class InterfaceController {

    @Autowired private ISubscribeContainer subscribeContainer;

    @Autowired private IInterfaceContainer interfaceContainer;

    @Autowired private IServiceProviderInvoker serviceProviderInvoker;

    @PostMapping("subscribes")
    public Result findSubscribes(@RequestParam("clazz") String className) {
        if (null == className) {
            return Result.error();
        }
        return Result.ok(subscribeContainer.findByClassName(className));
    }

    @PostMapping("testInvoke")
    public Result testInvoke(
            @RequestParam("provider") String provider,
            @RequestParam("className") String className,
            @RequestParam("methodName") String methodName,
            @RequestParam("arguments") String arguments) {
        List<InterfaceDTO> interfaceDTOS = interfaceContainer.findByInterfaceName(className);
        if (null == interfaceDTOS || interfaceDTOS.isEmpty()) {
            return Result.error("未找到服务提供者");
        }
        List<InterfaceDTO> providers =
                interfaceDTOS.stream()
                        .filter(it -> Objects.equals(it.getHost(), provider))
                        .collect(Collectors.toList());
        if (providers.isEmpty()) {
            return Result.error(provider + "未提供此服务");
        }
        InterfaceDTO interfaceDTO = providers.get(0);
        List<InterfaceMethodDTO> methods =
                interfaceDTO.getInterfaceMethodDTOS().stream()
                        .filter(it -> Objects.equals(it.getFullName(), methodName))
                        .collect(Collectors.toList());
        if (methods.isEmpty()) {
            return Result.error(className + "未提供" + methodName + "方法");
        }
        InterfaceMethodDTO methodDTO = methods.get(0);
        InvokeDTO invokeDTO = new InvokeDTO();
        invokeDTO.setArgumentValues(
                InvokeDTOUtil.getArgumentValues(methodDTO.getParameterTypeNames(), arguments));
        invokeDTO.setInterfaceName(className);
        invokeDTO.setMethodName(methodDTO.getMethodName());
        invokeDTO.setParameterTypeNames(methodDTO.getParameterTypeNames());
        invokeDTO.setHost(InvokeDTOUtil.getHost(provider));
        try {
            return Result.ok(serviceProviderInvoker.invoke(invokeDTO));
        } catch (Exception e) {
            if (log.isErrorEnabled()) {
                log.error("执行代码出错.", e);
            }
            return Result.error();
        }
    }
}
