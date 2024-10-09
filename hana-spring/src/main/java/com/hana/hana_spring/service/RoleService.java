package com.hana.hana_spring.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hana.hana_spring.dao.AuthMapper;
import com.hana.hana_spring.dao.RoleMapper;
import com.hana.hana_spring.entity.Auth;
import com.hana.hana_spring.entity.Role;

@Service
@Transactional(rollbackFor = Exception.class)
public class RoleService {

    @Autowired
    private AuthMapper auth_mapper;

    @Autowired
    private RoleMapper role_mapper;

    public List<Auth> get_all_auth() {
        List<Auth> auths = auth_mapper.sel_all();
        return auths;
    }

    public List<Role> get_all_role() {
        List<Role> roles = role_mapper.sel_all();
        return roles;
    }

    public void add_role(Role role) {
        if (role != null && role.getName() != null && !role.getName().isBlank()) {
            role_mapper.ins(role);
        }

        int rid = role.getId();
        if (role.getAuths() == null) {
            return;
        }
        role.getAuths().forEach((auth) -> {
            auth_mapper.ins_role_auth(rid, auth.getId());
        });
    }

    public void del_role(Integer rid) {
        if (rid == null) {
            return;
        }
        auth_mapper.del_role_all_auth(rid);
        role_mapper.del(rid);
    }

    public void upd_role(Role role) {
        if (role == null) {
            return;
        }
        role_mapper.upd(role);

        if (role.getAuths() == null) {
            return;
        }
        // 获取角色id
        int rid = role.getId();
        // 获取数据库记录的角色的权限
        List<Auth> db_auths = auth_mapper.sel_by_role_id(rid);
        // 获取更新的角色权限
        List<Auth> upd_auths = role.getAuths();
        // 获取数据库记录的角色权限的数量
        int db_len = db_auths.size();
        // 获取更新的角色权限数量
        int upd_len = upd_auths.size();

        int max = Math.max(db_len, upd_len);
        int min = Math.min(db_len, upd_len);

        for (int i = 0; i < max; i++) {
            if (i < min) {
                auth_mapper.upd_role_auth(rid, db_auths.get(i).getId(), upd_auths.get(i).getId());
            } else if (i < db_len) {
                auth_mapper.del_role_one_auth(rid, db_auths.get(i).getId());
            } else {
                auth_mapper.ins_role_auth(rid, upd_auths.get(i).getId());
            }
        }

    }
}
