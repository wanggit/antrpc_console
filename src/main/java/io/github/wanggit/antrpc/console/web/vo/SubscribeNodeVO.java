package io.github.wanggit.antrpc.console.web.vo;

import io.github.wanggit.antrpc.console.web.utils.LongToDateUtil;
import lombok.Data;

import java.io.Serializable;

@Data
public class SubscribeNodeVO implements Serializable {
    private static final long serialVersionUID = 6998859209070585823L;

    private String host;

    private Long ts;

    private String tsStr;

    public String getTsStr() {
        return LongToDateUtil.toDate(ts);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof SubscribeNodeVO)) {
            return false;
        }
        return host.equals(((SubscribeNodeVO) obj).getHost());
    }

    @Override
    public String toString() {
        return host;
    }

    @Override
    public int hashCode() {
        return host.hashCode();
    }
}
