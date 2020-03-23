package io.github.wanggit.antrpc.console.web.vo;

import io.github.wanggit.antrpc.console.zookeeper.InterfaceMethodDTO;
import lombok.Data;
import org.springframework.util.DigestUtils;

import java.io.Serializable;
import java.nio.charset.Charset;
import java.util.Set;
import java.util.TreeSet;

@Data
public class InterfaceVO implements Serializable {
    private static final long serialVersionUID = -1984104174001470207L;

    private String className;

    private String id;

    public void setClassName(String className) {
        this.className = className;
        id = DigestUtils.md5DigestAsHex(className.getBytes(Charset.forName("UTF-8")));
    }

    private Set<InterfaceHostVO> hosts =
            new TreeSet<>((o1, o2) -> o1.getHost().compareToIgnoreCase(o2.getHost()));

    private Set<InterfaceMethodDTO> interfaceMethodDTOS =
            new TreeSet<>((o1, o2) -> o1.toString().compareToIgnoreCase(o2.toString()));

    @Override
    public String toString() {
        return className;
    }

    @Override
    public int hashCode() {
        return className.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof InterfaceVO)) {
            return false;
        }
        InterfaceVO other = (InterfaceVO) obj;
        return other.getClassName().equals(className);
    }
}
