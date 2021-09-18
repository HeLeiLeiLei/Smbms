package com.hl.service.user;

import com.hl.dao.BaseDao;
import com.hl.dao.user.UserDao;
import com.hl.dao.user.UserDaoImpl;
import com.hl.pojo.User;
import com.hl.util.Constants;
import org.junit.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

public class UserServiceImpl implements UserService {

    //Service业务层 都会调用Dao层 所以我们要引入Dao层
    private UserDao userDao;
    public UserServiceImpl(){
        userDao=new UserDaoImpl();
    }

    public User Login(String userCode, String password) {
        Connection connection=null;
        User user=null;

        try {
            connection= BaseDao.getConnection();
            //通过业务层调用对应的数据库操作
            user = userDao.getLoginUser(connection, userCode);
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            BaseDao.closeResource(connection,null,null);
        }
        return user;
    }

    public int updateUserPassword(String newPasswrod,int id) {
        Connection connection=null;
        int num=0;
        try {
            connection= BaseDao.getConnection();
            num=userDao.udpdateUserPassword(connection,newPasswrod,id);
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            BaseDao.closeResource(connection,null,null);
        }
        return num;
    }

    public List<User> getUserList(String querUserName,
                                  int querUserRole,
                                  int currentPageNo,
                                  int pageSize) {
        Connection connection=null;
        List<User> userList=null;
        try{
            connection=BaseDao.getConnection();
            userList = userDao.getUserList(connection, querUserName, querUserRole, currentPageNo, pageSize);
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            BaseDao.closeResource(connection,null,null);
        }
        return userList;
    }

    public int getUserCount(String userName, int userRole) {
        Connection connection=null;
        int userCount=0;
        try {
            connection=BaseDao.getConnection();
            userCount = userDao.getUserCount(connection, userName, userRole);
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            BaseDao.closeResource(connection,null,null);
        }
        return userCount;
    }

    public int findUserbyUserCode(String userCode) {
        Connection  connection=null;
        int num=0;
        try {
            connection=BaseDao.getConnection();
            num= userDao.findUserbyUserCode(connection, userCode);
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            BaseDao.closeResource(connection,null,null);
        }
        return num;
    }

    public int addUser(User user) throws SQLException {
        Connection connection=null;
        int num=0;
        try {
            connection=BaseDao.getConnection();
            //开始事务
            connection.setAutoCommit(false);
            num = userDao.addUser(connection, user);

            //提交事务
            connection.commit();
        } catch (Exception e) {
            e.printStackTrace();
            //业务回滚
            connection.rollback();
        }finally {
            BaseDao.closeResource(connection,null,null);
        }
        return num;
    }

    public int deleteUser(int userId) throws SQLException {
        Connection connection=null;
        int num=0;
        try {
            connection=BaseDao.getConnection();
            connection.setAutoCommit(false);
            num=userDao.deleteUser(connection,userId);
            connection.commit();
        } catch (SQLException e) {
            e.printStackTrace();
            connection.rollback();
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            BaseDao.closeResource(connection,null,null);
        }
        return num;
    }
}
