package io.github.wanggit.antrpc.console.zookeeper;

import lombok.Data;

import java.io.Serializable;

@Data
public class InterfaceMethodDTO extends RegisterBeanMethod implements Serializable {
    private static final long serialVersionUID = -3738830970705099312L;

    private String className;

    @Override
    public String toString() {
        return super.toString();
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
