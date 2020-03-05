package io.github.wanggit.antrpc.console.zookeeper;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Objects;

@Data
public class RegisterBeanMethod {
    private String methodName;

    private List<String> parameterTypeNames;

    private int limit;

    private int durationInSeconds;

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
