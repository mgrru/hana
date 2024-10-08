package com.hana.hana_spring.entity.dto;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.hana.hana_spring.entity.Auth;
import com.hana.hana_spring.entity.Role;

import lombok.Data;

@Data
@Component
public class RoleReq {
    private Integer id;
    private String name;
    private List<Integer> permissions;

    public Role toRole() {
        List<Auth> auths = new ArrayList<>();
        for (Integer i : permissions) {
            Auth auth = new Auth(i, null);
            auths.add(auth);
        }
        return new Role(id, name, auths);
    }
}
