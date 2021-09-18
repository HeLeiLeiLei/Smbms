package com.hl.service.user;

import com.hl.pojo.User;

import java.sql.SQLException;
import java.util.List;

public interface UserService {
    //用户登录
    public User Login(String userCode,String password);
    //修改用户名密码
    public int updateUserPassword(String newPasswrod,int id);
    //根据条件获取用户列表
    public List<User> getUserList(String querUserName,
                                  int querUserRole,
                                  int currentPageNo,
                                  int pageSize);
    //查询记录数
    public int getUserCount(String userName,int userRole);

    //查询userCode 是否重名
    public int findUserbyUserCode(String userCode);

    //添加用户
    public int addUser(User user) throws SQLException;

    //删除用户
    public int deleteUser(int userId) throws SQLException;

    //修改用户
    public int updateUser(User user) throws SQLException;

    //显示用户数据
    public User showUser(String userCode);
}
