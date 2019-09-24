package org.javatribe.lottery.entity;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.javatribe.lottery.enums.ResultEnum;

/**
 * 返回结果类
 *
 * @author JimZiSing
 */
@Data
@ToString
public class Result<T> {
    private Integer code;
    private T data;
    private String message;

    public Result() {
    }

    public Result(int code, String message) {
        this.code = code;
        this.data = data;
        this.message = message;
    }

    public Result(int code, T data, String message) {
        this.code = code;
        this.data = data;
        this.message = message;
    }

    public static Result success() {
        return new Result(0, null, "success");
    }

    public static <T> Result success(T data) {
        return new Result(0, data, "success");
    }

    public static Result success(Integer code, String msg) {
        return new Result(code, msg);
    }

    public static <T> Result success(Integer code, T data, String msg) {
        return new Result(code, data, msg);
    }

    public static Result success(ResultEnum resultEnum) {
        return success(resultEnum.getCode(), resultEnum.getMsg());
    }

    public static <T> Result success(ResultEnum resultEnum, T data) {
        return success(resultEnum.getCode(), data, resultEnum.getMsg());
    }

    public static Result error(Integer code, String msg) {
        return new Result(code, msg);
    }

    public static Result error(String message) {
        return new Result(-1, message, null);
    }

    public static Result error(ResultEnum resultEnum) {
        return error(resultEnum.getCode(), resultEnum.getMsg());
    }

    public static <T> Result error(ResultEnum resultEnum, T data) {
        return new Result(resultEnum.getCode(), data, resultEnum.getMsg());
    }

}
