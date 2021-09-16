package com.hl.servlet.user;

import com.alibaba.fastjson.JSONArray;
import com.hl.pojo.Role;
import com.hl.pojo.User;
import com.hl.service.role.RoleService;
import com.hl.service.role.RoleServiceImpl;
import com.hl.service.user.UserService;
import com.hl.service.user.UserServiceImpl;
import com.hl.util.Constants;
import com.hl.util.PageSupport;


import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if(req.getParameter("method") != null && req.getParameter("method").equals("savepwd")){
            this.updateUserPassword(req,resp);
        }else if(req.getParameter("method") != null &&req.getParameter("method").equals("pwdmodify")){
            this.pwdModify(req,resp);
        }else if(req.getParameter("method") != null &&req.getParameter("method").equals("query")){
            this.query(req,resp);
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


    //查询用户信息
    public void query(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if(req.getSession().getAttribute(Constants.USER_SESSION) != null){
            //获取前端页面参数
            String querUserName=req.getParameter("queryname");
            String querUserRole=req.getParameter("queryUserRole");
            String pageIndex = req.getParameter("pageIndex");

            int UserRole=0;
            if(querUserRole != null && querUserRole.length()>0){
                UserRole=Integer.parseInt(querUserRole);
            }

            UserService userService=new UserServiceImpl();
            RoleService roleService=new RoleServiceImpl();

            //分页操作 第一次走这个请求，一定是第一页，页面大小固定的
            int currentPageNo=1;
            int pageSize=5;
            if(pageIndex != null){
                currentPageNo=Integer.parseInt(pageIndex);
            }
            //获取用户数量
            int totalCount = userService.getUserCount(querUserName, UserRole);
            //分页参数支持
            PageSupport pageSupport=new PageSupport();
            pageSupport.setCurrentPageNo(currentPageNo);
            pageSupport.setPageSize(pageSize);
            //设置总数量
            pageSupport.setTotalCount(totalCount);
            //设置总页数
            if(totalCount%pageSize ==0){
                pageSupport.setTotalPageCount(totalCount/pageSize);
            }else {
                pageSupport.setTotalPageCount((totalCount/pageSize)+1);
            }
            //上一页 下一页的约束
            if(currentPageNo<0){
                currentPageNo=1;
            }else if(currentPageNo > pageSupport.getTotalPageCount()){
                currentPageNo=pageSupport.getTotalPageCount();
            }
            //获取用户列表
            List<User> userList = userService.getUserList(querUserName, UserRole, currentPageNo, pageSize);
            //获取权限列表
            List<Role> roleList = roleService.getRoleList();

            req.setAttribute("userList",userList);
            req.setAttribute("roleList",roleList);
            req.setAttribute("queryUserName",querUserName);
            req.setAttribute("queryUserRole",querUserRole);
            req.setAttribute("totalCount",totalCount);
            req.setAttribute("currentPageNo",currentPageNo);
            req.setAttribute("totalPageCount",pageSupport.getTotalPageCount());
            req.getRequestDispatcher("/jsp/userlist.jsp").forward(req,resp);
        }
        }

}
