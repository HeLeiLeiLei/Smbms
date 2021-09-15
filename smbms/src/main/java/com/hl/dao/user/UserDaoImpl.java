package com.hl.dao.user;

import com.hl.dao.BaseDao;
import com.hl.pojo.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class UserDaoImpl implements UserDao{

    PreparedStatement pstm=null;
    ResultSet rs=null;
    User user=null;

    public User getLoginUser(Connection connection, String userCode) {

        if(connection !=null){
            String sql="select * from smbms_user where userCode=?";
            Object[] params={userCode};
            try {
                rs = BaseDao.executQ(connection, pstm, rs, params, sql);
                while (rs.next()){
                    user=new User();
                    user.setId(rs.getInt("id"));
                    user.setUserCode(rs.getString("userCode"));
                    user.setUserName(rs.getString("userName"));
                    user.setUserPassword(rs.getString("userPassword"));
                    user.setGender(rs.getInt("gender"));
                    user.setBirthday(rs.getDate("birthday"));
                    user.setPhone(rs.getString("phone"));
                    user.setAddress(rs.getString("address"));
                    user.setUserRole(rs.getInt("userRole"));
                    user.setCreatedBy(rs.getInt("createdBy"));
                    user.setCreationDate(rs.getTimestamp("creationDate"));
                    user.setModifyBy(rs.getInt("modifyBy"));
                    user.setModifyDate(rs.getTimestamp("modifyDate"));
                }
                BaseDao.closeResource(null,pstm,rs);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return user;
    }

    public int udpdateUserPassword(Connection connection,String userPassword,int id) {
        int num=0;
        if(connection != null){
            String sql="update smbms_user set userPassword=? where id=?";
            Object[] params={userPassword,id};
            try {
                num=BaseDao.executU(connection,pstm,params,sql);
            } catch (Exception e) {
                e.printStackTrace();
            }finally {
                BaseDao.closeResource(null,pstm,null);
            }
        }
        return num;
    }
}
