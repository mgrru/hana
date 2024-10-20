package com.hana.hana_spring.utils;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class Result {

    public static ResponseEntity<String> success() {
        Code c = Code.SUCCESS;
        return ResponseEntity.status(c.code()).body(c.msg());
    }

    public static ResponseEntity<String> success(Object data) {
        try {
            Code c = Code.SUCCESS;
            return ResponseEntity.status(c.code()).body(new ObjectMapper().writeValueAsString(data));
        } catch (JsonProcessingException e) {
            Code c = Code.RESULTERR;
            return ResponseEntity.status(c.code()).body(c.msg());
        }
    }

    public static ResponseEntity<String> error() {
        Code c = Code.ERROR;

        return ResponseEntity.status(c.code()).body(c.msg());
    }

    public static ResponseEntity<String> exception() {
        Code c = Code.EXCEPTION;

        return ResponseEntity.status(c.code()).body(c.msg());
    }

    public static ResponseEntity<String> no_auth() {
        Code c = Code.NOAUTH;

        return ResponseEntity.status(c.code()).body(c.msg());
    }

    public static ResponseEntity<String> no_email() {
        Code c = Code.NOEMAIL;

        return ResponseEntity.status(c.code()).body(c.msg());
    }

    public static ResponseEntity<String> email_err() {
        Code c = Code.NOEMAIL;

        return ResponseEntity.status(c.code()).body(c.msg());
    }

    public static ResponseEntity<String> no_user() {
        Code c = Code.NOUSER;

        return ResponseEntity.status(c.code()).body(c.msg());
    }
}
