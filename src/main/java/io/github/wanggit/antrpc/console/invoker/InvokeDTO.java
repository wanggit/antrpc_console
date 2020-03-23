package io.github.wanggit.antrpc.console.invoker;

import com.alibaba.fastjson.JSONObject;
import io.github.wanggit.antrpc.commons.bean.Host;
import io.github.wanggit.antrpc.commons.utils.CastValueTo;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.ClassUtils;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Data
public class InvokeDTO implements Serializable {

    private static final long serialVersionUID = -8915580980020559039L;

    private String provider;

    private String className;

    private String methodName;

    private String arguments;

    private List<String> parameterTypeNames;

    Host getHost() {
        if (null == provider) {
            throw new IllegalArgumentException("provider is null.");
        }
        String[] tmps = provider.split("@");
        if (tmps.length <= 1) {
            throw new IllegalArgumentException(
                    "provider format is \"applicationName@localhost:6060\"");
        }
        return Host.parse(tmps[1]);
    }

    Object[] getArgumentValues() {
        if (null == parameterTypeNames || parameterTypeNames.isEmpty()) {
            return new Object[0];
        }
        List<? extends Class<?>> types =
                parameterTypeNames.stream()
                        .map(
                                it -> {
                                    try {
                                        return ClassUtils.forName(
                                                it, InvokeDTO.class.getClassLoader());
                                    } catch (ClassNotFoundException e) {
                                        if (log.isErrorEnabled()) {
                                            log.error("not found the class.", e);
                                        }
                                        return null;
                                    }
                                })
                        .filter(Objects::nonNull)
                        .collect(Collectors.toList());
        if (types.size() != parameterTypeNames.size()) {
            throw new IllegalArgumentException(
                    "Has unknown parameter types." + JSONObject.toJSONString(parameterTypeNames));
        }
        String[] args = arguments.split("\\s*\\$\\$\\s*");
        Object[] realTypeArgs = new Object[args.length];
        for (int i = 0; i < args.length; i++) {
            realTypeArgs[i] = CastValueTo.cast(args[i].trim(), types.get(i));
        }
        return realTypeArgs;
    }
}
