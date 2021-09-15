package com.hl.dao.user;

import com.hl.pojo.User;

import java.sql.Connection;

public interface UserDao {
    //用户登录
    public User getLoginUser(Connection connection,String userCode);

    //修改密码
    public int udpdateUserPassword(Connection connection,String userPassword,int id);

}
