package io.github.wanggit.antrpc.console.web;

import io.github.wanggit.antrpc.console.web.vo.Result;
import io.github.wanggit.antrpc.console.zookeeper.IInterfaceContainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("interface")
public class InterfaceController {

    @Autowired private IInterfaceContainer interfaceContainer;

    @PostMapping("byName")
    public Result findByInterfaceName(
            @RequestParam(value = "name", defaultValue = "", required = false)
                    String interfaceName) {
        return Result.ok(interfaceContainer.findByInterfaceName(interfaceName));
    }

    @PostMapping("byMethodName")
    public Result findByInterfaceMethodName(
            @RequestParam(value = "name", defaultValue = "", required = false) String methodName) {
        return Result.ok(interfaceContainer.findByInterfaceMethodName(methodName));
    }
}
