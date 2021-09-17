package com.hl.dao.user;

import com.hl.pojo.User;

import java.sql.Connection;
import java.util.List;

public interface UserDao {
    //用户登录
    public User getLoginUser(Connection connection,String userCode);

    //修改密码
    public int udpdateUserPassword(Connection connection,String userPassword,int id);

    //获取用户列表
    public List<User> getUserList(Connection connection,
                               String userName,
                               int userRole,
                               int currentPageNo,
                               int pageSize);

    //根据用户名或者角色查询用户数量
    public int getUserCount(Connection connection,String userName,int userRole);

    //添加用户
    public int addUser(Connection connection,User user);
}
