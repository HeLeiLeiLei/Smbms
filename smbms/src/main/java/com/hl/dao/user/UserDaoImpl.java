package com.hl.dao.user;

import com.hl.dao.BaseDao;
import com.hl.pojo.User;
import com.mysql.jdbc.StringUtils;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

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

    public int getUserCount(Connection connection, String userName, int userRole) {
        int count=0;
        if(connection != null){
            StringBuffer sql=new StringBuffer();
            ArrayList<Object> list = new ArrayList<Object>();
            sql.append("select count(1) as count from smbms_user u,smbms_role r where u.userRole=r.id");

            if(userName != null && userName.length()>0){
                sql.append(" and u.userName like ?");
                list.add("%"+userName+"%");
            }
            if(userRole > 0){
                sql.append(" and u.userRole=?");
                list.add(userRole);
            }
            //怎么把list转换为数组
            Object[] array = list.toArray();
            try {
                rs = BaseDao.executQ(connection, pstm, rs, array, sql.toString());
                if(rs.next()){
                    count = rs.getInt("count");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }finally {
                BaseDao.closeResource(null,pstm,rs);
            }
        }
        return count;
    }

    public List<User> getUserList(Connection connection, String userName, int userRole, int currentPageNo, int pageSize) {
        ArrayList<Object> list = new ArrayList<Object>();
        List<User> userList=new ArrayList<User>();
        if(connection != null){
            StringBuffer sql=new StringBuffer();
            sql.append("select u.*,r.roleName as userRoleName from smbms_user u,smbms_role r where u.userRole = r.id");
            if(!StringUtils.isNullOrEmpty(userName)){
                sql.append(" and u.userName like ?");
                list.add("%"+userName+"%");
            }
            if(userRole > 0){
                sql.append(" and u.userRole=?");
                list.add(userRole);
            }
            sql.append(" order by id asc  limit ?,?");
            currentPageNo=(currentPageNo-1)*pageSize;
            list.add(currentPageNo);
            list.add(pageSize);

            Object[] array = list.toArray();
            try{
                rs = BaseDao.executQ(connection, pstm, rs, array, sql.toString());
                while (rs.next()){
                    User user = new User();
                    user.setUserCode(rs.getString("userCode"));
                    user.setUserName(rs.getString("userName"));
                    user.setGender(rs.getInt("gender"));
                    user.setBirthday(rs.getDate("birthday"));
                    user.setPhone(rs.getString("phone"));
                    user.setUserRoleName(rs.getString("userRoleName"));
                    userList.add(user);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }finally {
                BaseDao.closeResource(null,pstm,rs);
            }
        }


        return userList;
    }

    public int addUser(Connection connection,User user){
        String sql="insert into smbms_user(?,?,?,?,?,?,?,?,?) values(?,?,?,?,?,?,?,?,?)";
        return 0;
    }
}
