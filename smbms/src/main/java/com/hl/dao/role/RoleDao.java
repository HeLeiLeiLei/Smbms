package com.hl.dao.role;

import com.hl.pojo.Role;

import java.sql.Connection;
import java.util.List;

public interface RoleDao{
    //获取Role列表信息
    public List<Role> getRoleList(Connection connection);
}
