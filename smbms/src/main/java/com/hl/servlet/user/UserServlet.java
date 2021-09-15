package com.hl.servlet.user;

import com.alibaba.fastjson.JSONArray;
import com.hl.pojo.User;
import com.hl.service.user.UserService;
import com.hl.service.user.UserServiceImpl;
import com.hl.util.Constants;


import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

public class UserServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if(req.getParameter("method") != null && req.getParameter("method").equals("savepwd")){
            this.updateUserPassword(req,resp);
        }else if(req.getParameter("method") != null &&req.getParameter("method").equals("pwdmodify")){
            this.pwdModify(req,resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }

    //修改当前用户密码
    public void updateUserPassword(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //获取页面参数
        String newpassword=req.getParameter("newpassword");
        String rnewpassword=req.getParameter("rnewpassword");
        //通过session获取以前的oldpassword
        User user= (User) req.getSession().getAttribute(Constants.USER_SESSION);
        UserService userService = new UserServiceImpl();
        //判断用户传过来的旧密码密码是否相同
        if(user != null){
            if(newpassword.length()>0 && newpassword != null
                    &&rnewpassword.length()>0 && rnewpassword != null
                    && newpassword.equals(rnewpassword)
            ){
                int i = userService.updateUserPassword(newpassword,user.getId());
                if(i>0){
                    req.setAttribute("message","密码修改成功,请退出系统重新登录");
                    req.getSession().removeAttribute(Constants.USER_SESSION);
                }else{
                    req.setAttribute("message","密码修改失败");
                }
            }else {
                req.setAttribute("message","新密码或确认密码有问题");
            }
            req.getRequestDispatcher("/jsp/pwdmodify.jsp").forward(req,resp);
        }else{
            req.getRequestDispatcher("/login.jsp").forward(req,resp);
        }
    }


    //验证旧密码
    public void pwdModify(HttpServletRequest req, HttpServletResponse resp){
        String oldpassword = req.getParameter("oldpassword");
        Object attribute = req.getSession().getAttribute(Constants.USER_SESSION);
        Map<String, String> resultmap = new HashMap<String,String>();
        if(attribute == null){
            resultmap.put("result","sessionerror");
        }else if(oldpassword==null){
            resultmap.put("result","error");
        }else if(((User)attribute).getUserPassword().equals(oldpassword)){
            resultmap.put("result","true");
        }else{
            resultmap.put("result","false");
        }
        try {
            resp.setContentType("application/json");
            PrintWriter printWriter = resp.getWriter();
            printWriter.write(JSONArray.toJSONString(resultmap));
            printWriter.flush();
            printWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
