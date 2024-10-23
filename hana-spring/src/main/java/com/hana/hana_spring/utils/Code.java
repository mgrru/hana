package com.hana.hana_spring.utils;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum Code {
    SUCCESS(200, "成功"),
    ERROR(444, "错误"),
    EXCEPTION(555, "异常"),
    NOAUTH(666, "无权限"),
    NOEMAIL(466, "没有邮箱"),
    CODEERR(333, "验证码错误"),
    RESULTERR(455, "结果出错"),
    NOUSER(477, "用户已封禁"),
    PASSERR(255, "密码错误"),
    NEWPASSERR(266, "密码相同");

    private final int code;
    private final String msg;

    public int code() {
        return code;
    }

    public String msg() {
        return msg;
    }
}
