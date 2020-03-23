package io.github.wanggit.antrpc.console.web.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class Result implements Serializable {

    private static final long serialVersionUID = 6225425118291438805L;
    private int code;

    private String message;

    private Object data;

    public static Result ok() {
        Result result = new Result();
        result.setCode(ResultConstants.SUCCESS);
        return result;
    }

    public static Result ok(Object data) {
        Result result = new Result();
        result.setCode(ResultConstants.SUCCESS);
        result.setData(data);
        return result;
    }

    public static Result error() {
        return error(ResultConstants.DEFAULT_ERROR);
    }

    public static Result error(String message) {
        Result result = new Result();
        result.setCode(ResultConstants.ERROR);
        result.setMessage(message);
        return result;
    }
}
