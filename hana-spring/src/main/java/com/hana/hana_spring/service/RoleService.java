package com.hana.hana_spring.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hana.hana_spring.dao.RoleMapper;
import com.hana.hana_spring.entity.Role;

@Service
@Transactional(rollbackFor = Exception.class)
public class RoleService {

    @Autowired
    private RoleMapper role_mapper;

    public List<Role> get_all_role() {
        List<Role> roles = role_mapper.sel_all();
        return roles;
    }

    public void add_role(Role role) {
        if (role != null && role.getName() != null && !role.getName().isBlank()) {
            role_mapper.ins(role);
        }
    }

    public void del_role(Integer rid) {
        if (rid == null) {
            return;
        }
        role_mapper.del(rid);
    }

    public void upd_role(Role role) {
        if (role == null) {
            return;
        }
        role_mapper.upd(role);
    }
}
