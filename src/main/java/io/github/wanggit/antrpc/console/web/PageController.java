package io.github.wanggit.antrpc.console.web;

import io.github.wanggit.antrpc.console.web.vo.InterfaceHostVO;
import io.github.wanggit.antrpc.console.web.vo.InterfaceVO;
import io.github.wanggit.antrpc.console.zookeeper.IInterfaceContainer;
import io.github.wanggit.antrpc.console.zookeeper.InterfaceDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

@Controller
@RequestMapping
public class PageController {

    @Autowired private IInterfaceContainer interfaceContainer;

    @RequestMapping()
    public String index(
            @RequestParam(value = "name", defaultValue = "", required = false) String name,
            Model model) {
        List<InterfaceDTO> interfaceDTOS = interfaceContainer.findByInterfaceName(name);
        TreeSet<InterfaceVO> interfaceVOS =
                new TreeSet<>((o1, o2) -> o1.getClassName().compareToIgnoreCase(o2.getClassName()));
        Map<String, InterfaceVO> datas = new HashMap<>();
        interfaceDTOS.forEach(
                it -> {
                    if (!datas.containsKey(it.getClassName())) {
                        InterfaceVO interfaceVO = new InterfaceVO();
                        interfaceVO.setClassName(it.getClassName());
                        datas.put(it.getClassName(), interfaceVO);
                    }
                    InterfaceVO interfaceVO = datas.get(it.getClassName());
                    InterfaceHostVO hostVO = new InterfaceHostVO();
                    hostVO.setHost(it.getHost());
                    hostVO.setPath(it.getPath());
                    hostVO.setRegisterTs(it.getRegisterTs());
                    interfaceVO.getHosts().add(hostVO);
                    interfaceVO.getInterfaceMethodDTOS().addAll(it.getInterfaceMethodDTOS());
                });
        interfaceVOS.addAll(datas.values());
        model.addAttribute("condition", name);
        model.addAttribute("interfaces", interfaceVOS);
        return "index";
    }
}
