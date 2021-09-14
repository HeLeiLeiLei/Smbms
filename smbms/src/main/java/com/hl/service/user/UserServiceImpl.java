package com.hl.service.user;

import com.hl.dao.BaseDao;
import com.hl.dao.user.UserDao;
import com.hl.dao.user.UserDaoImpl;
import com.hl.pojo.User;
import org.junit.Test;

import java.sql.Connection;

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

//    @Test
//    public void test(){
//        UserServiceImpl userService = new UserServiceImpl();
//        User user=userService.Login("admin","123456asdasd");
//        System.out.println(user.getUserPassword());
//    }
}
