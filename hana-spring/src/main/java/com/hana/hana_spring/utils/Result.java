package com.hana.hana_spring.utils;

import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Component
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Result {
    private int code;
    private String msg;
    private String data;

    public static Result success() {
        Code c = Code.SUCCESS;
        return new Result(c.code(), c.msg(), null);
    }

    public static Result success(String data) {
        Code c = Code.SUCCESS;
        return new Result(c.code(), c.msg(), data);
    }

    public static Result error() {
        Code c = Code.ERROR;
        return new Result(c.code(), c.msg(), null);
    }

    public static Result exception() {
        Code c = Code.EXCEPTION;
        return new Result(c.code(), c.msg(), null);
    }

    public static Result noauth() {
        Code c = Code.NOAUTH;
        return new Result(c.code(), c.msg(), null);
    }

    public static Result noemail() {
        Code c = Code.NOEMAIL;
        return new Result(c.code(), c.msg(), null);
    }
}
