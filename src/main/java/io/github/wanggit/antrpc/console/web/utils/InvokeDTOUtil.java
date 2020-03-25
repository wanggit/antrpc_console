package io.github.wanggit.antrpc.console.web.utils;

import com.alibaba.fastjson.JSONObject;
import io.github.wanggit.antrpc.commons.bean.Host;
import io.github.wanggit.antrpc.commons.codec.serialize.json.JsonSerializer;
import io.github.wanggit.antrpc.commons.utils.CastValueTo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.ClassUtils;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
public abstract class InvokeDTOUtil {

    public static Host getHost(String provider) {
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

    public static Object[] getArgumentValues(List<String> parameterTypeNames, String arguments) {
        if (null == parameterTypeNames || parameterTypeNames.isEmpty()) {
            return new Object[0];
        }
        List<? extends Class<?>> types =
                parameterTypeNames.stream()
                        .map(
                                it -> {
                                    try {
                                        return ClassUtils.forName(
                                                it, InvokeDTOUtil.class.getClassLoader());
                                    } catch (ClassNotFoundException e) {
                                        if (log.isWarnEnabled()) {
                                            log.warn(
                                                    it
                                                            + " not found the class. will use "
                                                            + JsonSerializer.class.getName()
                                                            + " repeat it.",
                                                    e);
                                        }
                                        return Map.class;
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
