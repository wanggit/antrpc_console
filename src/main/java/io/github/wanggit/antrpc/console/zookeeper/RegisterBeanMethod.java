package io.github.wanggit.antrpc.console.zookeeper;

import io.github.wanggit.antrpc.console.zookeeper.utils.ArrayClassNameUtil;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.DigestUtils;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Data
public class RegisterBeanMethod {
    private String methodName;

    private List<String> parameterTypeNames;

    private int limit;

    private int durationInSeconds;

    private String fullName;

    private String methodId;

    void generateMethodId() {
        StringBuilder buffer = new StringBuilder(methodName);
        for (String parameterTypeName : parameterTypeNames) {
            buffer.append(parameterTypeName);
        }
        methodId = DigestUtils.md5DigestAsHex(buffer.toString().getBytes(Charset.forName("UTF-8")));
    }

    public String getFullName() {
        // 清理一下参数类型
        List<String> types = new ArrayList<>(parameterTypeNames.size());
        parameterTypeNames.forEach(
                it -> {
                    types.add(ArrayClassNameUtil.replaceArrayClassName(it));
                });
        return methodName + "(" + StringUtils.join(types, ", ") + ")";
    }

    @Override
    public String toString() {
        return methodName
                + "("
                + (null == parameterTypeNames ? "" : StringUtils.join(parameterTypeNames, ","))
                + ") limit="
                + limit
                + ", durationInSeconds="
                + durationInSeconds;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof RegisterBeanMethod)) {
            return false;
        }
        RegisterBeanMethod other = (RegisterBeanMethod) obj;
        return Objects.equals(methodName, other.getMethodName())
                && Objects.equals(
                        parameterTypeNames.toString(), other.getParameterTypeNames().toString())
                && limit == other.getLimit()
                && durationInSeconds == other.getDurationInSeconds();
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
