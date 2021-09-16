package com.hl.service.role;

import com.hl.dao.BaseDao;
import com.hl.dao.role.RoleDao;
import com.hl.dao.role.RoleDaoImpl;
import com.hl.pojo.Role;
import org.junit.Test;

import java.sql.Connection;
import java.util.List;

public class RoleServiceImpl implements RoleService {

    private RoleDao roleDao;
    public RoleServiceImpl() {
        roleDao=new RoleDaoImpl();
    }

    public List<Role> getRoleList() {
        Connection connection=null;
        List<Role> roleList=null;
        try {
            connection= BaseDao.getConnection();
            roleList= roleDao.getRoleList(connection);
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            BaseDao.closeResource(connection,null,null);
        }
        return roleList;
    }
}
