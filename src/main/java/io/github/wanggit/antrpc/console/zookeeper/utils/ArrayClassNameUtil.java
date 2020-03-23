package io.github.wanggit.antrpc.console.zookeeper.utils;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class ArrayClassNameUtil {

    private static final Map<String, String> JAVA_ARRAY_NAME_TO_HUMAN_NAMES;

    static {
        Map<String, String> m = new HashMap<>();
        m.put("[I", "int[]");
        m.put("[Z", "boolean[]");
        m.put("[F", "float[]");
        m.put("[J", "long[]");
        m.put("[S", "short[]");
        m.put("[B", "byte[]");
        m.put("[D", "double[]");
        m.put("[C", "char[]");
        JAVA_ARRAY_NAME_TO_HUMAN_NAMES = Collections.unmodifiableMap(m);
    }

    public static String replaceArrayClassName(String type) {
        String result = JAVA_ARRAY_NAME_TO_HUMAN_NAMES.get(type.trim());
        if (null != result) {
            return result;
        }
        // [Ljava.lang.Integer; --> java.lang.Integer[]
        if (type.trim().matches("^\\[L[\\w._$]+;$")) {
            return type.substring(2, type.length() - 1) + "[]";
        }
        return type;
    }
}
