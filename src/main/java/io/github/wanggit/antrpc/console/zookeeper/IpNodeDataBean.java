package io.github.wanggit.antrpc.console.zookeeper;

import lombok.Data;

@Data
public class IpNodeDataBean {
    private Long ts;
    private String appName;
    private Integer rpcPort;
}
