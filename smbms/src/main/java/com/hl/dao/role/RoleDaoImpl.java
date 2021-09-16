package com.hl.dao.role;

import com.hl.dao.BaseDao;
import com.hl.pojo.Role;
import org.junit.runner.Result;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class RoleDaoImpl implements RoleDao {

    PreparedStatement pstm=null;
    ResultSet rs=null;
    Role role=null;
    public List<Role> getRoleList(Connection connection) {
        List<Role> list=new ArrayList<Role>();
        if(connection != null){
            String sql="select * from smbms_role";
            Object params[]={};
            try{
                rs=BaseDao.executQ(connection,pstm,rs,params,sql);
                while (rs.next()){
                    role=new Role();
                    role.setId(rs.getInt("id"));
                    role.setRoleCode(rs.getString("roleCode"));
                    role.setRoleName(rs.getString("roleName"));
                    list.add(role);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }finally {
                BaseDao.closeResource(null,pstm,rs);
            }
        }
        return list;
    }
}
