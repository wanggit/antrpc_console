package io.github.wanggit.antrpc.console.web.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class InterfaceHostVO implements Serializable {

    private static final long serialVersionUID = 1183498494035131199L;

    private String host;

    private Long registerTs;

    private String path;
}
