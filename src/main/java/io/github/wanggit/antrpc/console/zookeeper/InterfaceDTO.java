package io.github.wanggit.antrpc.console.zookeeper;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Data
class InterfaceDTO implements Serializable {

    private static final long serialVersionUID = 4932052226308856680L;
    private String className;

    private String host;

    private Long registerTs;

    private String path;

    private List<InterfaceMethodDTO> interfaceMethodDTOS = new ArrayList<>();

    public void addInterfaceMethodDTO(InterfaceMethodDTO interfaceMethodDTO) {
        interfaceMethodDTOS.add(interfaceMethodDTO);
    }

    @Override
    public String toString() {
        return className + "@" + host + "[" + path + "]";
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof InterfaceDTO)) {
            return false;
        }
        InterfaceDTO other = (InterfaceDTO) obj;
        return Objects.equals(other.getClassName(), className)
                && Objects.equals(other.getHost(), host)
                && Objects.equals(other.getPath(), path);
    }

    @Override
    public int hashCode() {
        return className.hashCode() + host.hashCode() + path.hashCode();
    }
}
