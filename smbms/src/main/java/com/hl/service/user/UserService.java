package com.hl.service.user;

import com.hl.pojo.User;

public interface UserService {
    //用户登录
    public User Login(String userCode,String password);
    //修改用户名密码
    public int updateUserPassword(String newPasswrod,int id);
}
