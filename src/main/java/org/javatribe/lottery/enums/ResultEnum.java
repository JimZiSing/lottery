package org.javatribe.lottery.enums;


/**
 * controller 返回数据结果集
 * @author JimZiSing
 */

public enum ResultEnum {
    UN_ERROR(-1, "未知错误"),
    SUCCESS(0, "success"),
    ERROR(40000, "没有找到api"),
    NOT_LOGIN(40001, "您还没有登录"),
    NOT_PRIZE(40002, "没有抽奖活动"),
    ERROR_MD5(40003, "MD5错误"),
    NOT_IN_TIME(40004, "不在抽奖时间"),
    SERVER_ERROR(50000, "服务器出错");

    private Integer code;
    private String msg;

    ResultEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
