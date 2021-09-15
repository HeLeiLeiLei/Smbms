package com.hl.servlet.user;

import com.hl.util.Constants;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

//退出登录 移除session中的用户信息
public class LoginOutServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Object user_session = req.getSession().getAttribute(Constants.USER_SESSION);
        if(user_session != null){
            req.getSession().removeAttribute(Constants.USER_SESSION);
            resp.sendRedirect(req.getContextPath()+"/login.jsp");
        }else{
            resp.sendRedirect(req.getContextPath()+"/error.jsp");
        }
    }
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }
}
