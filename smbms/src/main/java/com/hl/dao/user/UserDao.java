package com.hl.dao.user;

import com.hl.pojo.User;

import java.sql.Connection;

public interface UserDao {
    public User getLoginUser(Connection connection,String userCode);
}
