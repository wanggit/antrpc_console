package io.github.wanggit.antrpc.console.web.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class LongToDateUtil {

    public static String toDate(Long value) {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(value));
    }
}
