package com.hl.dao.user;

import com.hl.dao.BaseDao;
import com.hl.pojo.User;
import com.mysql.jdbc.StringUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
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
                    user.setId(rs.getInt("id"));
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

    public int findUserbyUserCode(Connection connection, String userCode) {
        String sql="select * from smbms_user where userCode=?";
        Object pamars[]={userCode};
        int num=0;
        if(connection != null){
            try {
                rs = BaseDao.executQ(connection, pstm, rs, pamars, sql);
                while (rs.next()){
                    if(rs.getString("userCode").equals(userCode)){
                        num++;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }finally {
                BaseDao.closeResource(null,pstm,rs);
            }
        }
        return num;
    }

    public int addUser(Connection connection, User user){
        String sql="insert into smbms_user(userCode,userName,userPassword,gender,birthday,phone,address,userRole,createdBy,creationDate) values(?,?,?,?,?,?,?,?,?,?)";
        int num=0;
        if(connection != null){
            List<Object> list = new ArrayList<Object>();
            list.add(user.getUserCode());
            list.add(user.getUserName());
            list.add(user.getUserPassword());
            list.add(user.getGender());
            list.add(user.getBirthday());
            list.add(user.getPhone());
            list.add(user.getAddress());
            list.add(user.getUserRole());
            list.add(user.getCreatedBy());
            list.add(user.getCreationDate());
            //把集合变成数组
            Object[] pamars = list.toArray();
            try {
                num = BaseDao.executU(connection, pstm, pamars, sql);
            } catch (Exception e) {
                e.printStackTrace();
            }finally {
                BaseDao.closeResource(null,pstm,null);
            }
        }
        return num;
    }

    public int deleteUser(Connection connection, int userId) {
        int num=0;
        if(connection != null){
            String sql="delete from smbms_user where id=?";
            Object pamars[]={userId};
            try{
                num = BaseDao.executU(connection, pstm, pamars, sql);
            } catch (Exception e) {
                e.printStackTrace();
            }finally {
                BaseDao.closeResource(null,pstm,null);
            }
        }
        return num;
    }

    public int updateUser(Connection connection, User user) {
        String sql="update smbms_user set userName=?,gender=?,birthday=?," +
                "phone=?,address=?,userRole=? where id=?";
        int num=0;
        ArrayList<Object> arrayList = new ArrayList<Object>();
        arrayList.add(user.getUserName());
        arrayList.add(user.getGender());
        arrayList.add(user.getBirthday());
        arrayList.add(user.getPhone());
        arrayList.add(user.getAddress());
        arrayList.add(user.getUserRole());
        arrayList.add(user.getId());
        Object[] pamars = arrayList.toArray();
        if(connection != null){
            try {
                num=BaseDao.executU(connection,pstm,pamars,sql);
            } catch (Exception e) {
                e.printStackTrace();
            }finally {
                BaseDao.closeResource(null,pstm,null);
            }
        }
        return num;
    }

    public User showUser(Connection connection,String userCode){
        String sql="select * from smbms_user where userCode=?";
        Object pamars[]={userCode};
        User user=new User();
        if(connection != null){
            try{
                rs = BaseDao.executQ(connection, pstm, rs, pamars, sql);
                while (rs.next()){
                    user.setId(rs.getInt("id"));
                    user.setUserName(rs.getString("userName"));
                    user.setGender(rs.getInt("gender"));
                    user.setBirthday(rs.getDate("birthday"));
                    user.setPhone(rs.getString("phone"));
                    user.setAddress(rs.getString("address"));
                    user.setUserRole(rs.getInt("userRole"));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }finally {
                BaseDao.closeResource(null,pstm,rs);
            }
        }

        return user;
    }
}
