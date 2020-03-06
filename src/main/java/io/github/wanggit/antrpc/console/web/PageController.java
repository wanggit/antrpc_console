package io.github.wanggit.antrpc.console.web;

import io.github.wanggit.antrpc.console.zookeeper.IInterfaceContainer;
import io.github.wanggit.antrpc.console.zookeeper.InterfaceDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping
public class PageController {

    @Autowired private IInterfaceContainer interfaceContainer;

    @RequestMapping()
    public String index(
            @RequestParam(value = "name", defaultValue = "", required = false) String name,
            Model model) {
        List<InterfaceDTO> interfaceDTOS = interfaceContainer.findByInterfaceName(name);
        model.addAttribute("condition", name);
        model.addAttribute("interfaces", interfaceDTOS);
        return "index";
    }
}
