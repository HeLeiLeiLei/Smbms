package com.hl.servlet.user;

import com.hl.pojo.User;
import com.hl.service.user.UserService;
import com.hl.service.user.UserServiceImpl;
import com.hl.util.Constants;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class LoginServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //获取用户输入的用户名和密码
        String userCode = req.getParameter("userCode");
        String userPassword = req.getParameter("userPassword");
        //和数据库中的密码进行对比,调用业务层：
        UserService userService = new UserServiceImpl();
        User user = userService.Login(userCode, userPassword);
        if(user != null){
            //将用户的信息放在Session中
            req.getSession().setAttribute(Constants.USER_SESSION,user);
            //跳转主页
            resp.sendRedirect(req.getContextPath()+"/jsp/frame.jsp");
        }else{
            //请求转发
            req.setAttribute("error","用户名或密码错误");
            req.getRequestDispatcher("login.jsp").forward(req,resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }
}
