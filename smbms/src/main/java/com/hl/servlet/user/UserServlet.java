package com.hl.servlet.user;

import com.alibaba.fastjson.JSONArray;
import com.hl.pojo.Role;
import com.hl.pojo.User;
import com.hl.service.role.RoleService;
import com.hl.service.role.RoleServiceImpl;
import com.hl.service.user.UserService;
import com.hl.service.user.UserServiceImpl;
import com.hl.util.Constants;
import com.hl.util.Page;



import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Date;


public class UserServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if(req.getParameter("method") != null && req.getParameter("method").equals("savepwd")){
            this.updateUserPassword(req,resp);
        }else if(req.getParameter("method") != null &&req.getParameter("method").equals("pwdmodify")){
            this.pwdModify(req,resp);
        }else if(req.getParameter("method") != null &&req.getParameter("method").equals("query")){
            this.query(req,resp);
        }else if(req.getParameter("method") != null && req.getParameter("method").equals("getrolelist")){
            this.getRoleList(req,resp);
        }else if(req.getParameter("method") != null && req.getParameter("method").equals("ucexist")){
            this.getUserCode(req,resp);
        }else if(req.getParameter("method") != null && req.getParameter("method").equals("add")){
            this.addUser(req,resp);
        }else if(req.getParameter("method") != null && req.getParameter("method").equals("deleteUser")){
            this.deleteUser(req,resp);
        }else if(req.getParameter("method") != null && req.getParameter("method").equals("updateUser")){
            this.updateUser(req,resp);
        }else if(req.getParameter("method") != null && req.getParameter("method").equals("modify")){
            this.showUser(req,resp);
        }else if(req.getParameter("method") != null && req.getParameter("method").equals("view")) {
            this.showUser(req,resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }

    //????????????????????????
    public void updateUserPassword(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //??????????????????
        String newpassword=req.getParameter("newpassword");
        String rnewpassword=req.getParameter("rnewpassword");
        //??????session???????????????oldpassword
        User user= (User) req.getSession().getAttribute(Constants.USER_SESSION);
        UserService userService = new UserServiceImpl();
        //???????????????????????????????????????????????????
        if(user != null){
            if(newpassword.length()>0 && newpassword != null
                    &&rnewpassword.length()>0 && rnewpassword != null
                    && newpassword.equals(rnewpassword)
            ){
                int i = userService.updateUserPassword(newpassword,user.getId());
                if(i>0){
                    req.setAttribute("message","??????????????????,???????????????????????????");
                    req.getSession().removeAttribute(Constants.USER_SESSION);
                }else{
                    req.setAttribute("message","??????????????????");
                }
            }else {
                req.setAttribute("message","?????????????????????????????????");
            }
            req.getRequestDispatcher("/jsp/pwdmodify.jsp").forward(req,resp);
        }
    }


    //???????????????
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


    //????????????????????????
    public void query(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if(req.getSession().getAttribute(Constants.USER_SESSION) != null){
            //????????????????????????
            String querUserName=req.getParameter("queryname");
            String querUserRole=req.getParameter("queryUserRole");
            String crtPage = req.getParameter("currentPage");

            int UserRole=0;
            if(querUserRole != null && querUserRole.length()>0){
                UserRole=Integer.parseInt(querUserRole);
            }

            UserService userService=new UserServiceImpl();
            RoleService roleService=new RoleServiceImpl();

            //???????????? ?????????????????????????????????????????????????????????????????????
            int currentPage=1;
            int pageSize=5;
            //??????????????????
            int totalCount = userService.getUserCount(querUserName, UserRole);
            Page page=new Page(currentPage,pageSize,totalCount);

            if(crtPage != null && crtPage.length()>0){
                page.setCurrentPage(Integer.parseInt(crtPage));
                page.setStart(Integer.parseInt(crtPage));
            }

            //??????????????????
            List<User> userList = userService.getUserList(querUserName, UserRole, page.getStart(), page.getPageSize());
            //??????????????????
            List<Role> roleList = roleService.getRoleList();
            req.setAttribute("userList",userList);
            req.setAttribute("roleList",roleList);
            req.setAttribute("queryUserName",querUserName);
            req.setAttribute("queryUserRole",querUserRole);
            req.setAttribute("page",page);
            req.getRequestDispatcher("/jsp/userlist.jsp").forward(req,resp);
        }
    }

    //????????????????????????
    public void getRoleList(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        RoleService roleService=new RoleServiceImpl();
        List<Role> roleList = roleService.getRoleList();
        resp.setContentType("application/json");
        PrintWriter writer = resp.getWriter();
        writer.write(JSONArray.toJSONString(roleList));
        writer.flush();
        writer.close();
    }

    //??????????????????
    public void getUserCode(HttpServletRequest req, HttpServletResponse resp){
        String userCode=req.getParameter("userCode");
        if(userCode != null && userCode.length()>0){
            UserService userService=new UserServiceImpl();
            int num = userService.findUserbyUserCode(userCode);

            resp.setContentType("application/json");
            Map<String, String> hashMap = new HashMap<String, String>();
            try {
                PrintWriter writer = resp.getWriter();
                if(num>0){
                    hashMap.put("userCode","exist");
                }
                writer.write(JSONArray.toJSONString(hashMap));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    //????????????
    public void addUser(HttpServletRequest req, HttpServletResponse resp){
        String userCode = req.getParameter("userCode");
        String userName = req.getParameter("userName");
        String userPassword = req.getParameter("userPassword");
        int gender=Integer.parseInt(req.getParameter("gender"));
        String birthday = req.getParameter("birthday");
        String phone = req.getParameter("phone");
        String address = req.getParameter("address");
        int userRole = Integer.parseInt(req.getParameter("userRole"));

        User user=new User();
        user.setUserCode(userCode);
        user.setUserName(userName);
        user.setUserPassword(userPassword);
        user.setGender(gender);
        try{
            user.setBirthday(new SimpleDateFormat("yyyy-MM-dd").parse(birthday));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        user.setPhone(phone);
        user.setAddress(address);
        user.setUserRole(userRole);
        user.setCreatedBy(((User)req.getSession().getAttribute(Constants.USER_SESSION)).getId());
        user.setCreationDate(new Date());
        UserService userService=new UserServiceImpl();
        try {
            int i = userService.addUser(user);
            if(i>0){
                resp.sendRedirect(req.getContextPath()+"/sys/userDao.dao?method=query");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    //????????????
    public void deleteUser(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        int userid = Integer.parseInt(req.getParameter("uid"));
        PrintWriter writer = resp.getWriter();
        resp.setContentType("application/json");
        HashMap<String, String> hashMap = new HashMap<String, String>();
        if(userid > 0){
            UserService userService=new UserServiceImpl();
            try{
                if(userService.deleteUser(userid) >0){
                    hashMap.put("delResult","true");
                }else {
                    hashMap.put("delResult","false");
                }
                writer.write(JSONArray.toJSONString(hashMap));
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }else {
            hashMap.put("delResult","notexist");
            writer.write(JSONArray.toJSONString(hashMap));
        }
    }

    //??????????????????
    public void updateUser(HttpServletRequest req, HttpServletResponse resp){
        String uid = req.getParameter("uid");
        String userName = req.getParameter("userName");
        String gender = req.getParameter("gender");
        String birthday = req.getParameter("birthday");
        String phone = req.getParameter("phone");
        String address = req.getParameter("address");
        String userRole = req.getParameter("userRole");

        User user=new User();
        user.setId(Integer.parseInt(uid));
        user.setUserName(userName);
        user.setGender(Integer.parseInt(gender));
        try {
            user.setBirthday(new SimpleDateFormat("yyyy-MM-dd").parse(birthday));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        user.setPhone(phone);
        user.setAddress(address);
        user.setUserRole(Integer.parseInt(userRole));

        UserService userService=new UserServiceImpl();
        try{
           int num= userService.updateUser(user);
           if(num > 0){
               resp.sendRedirect(req.getContextPath()+"/sys/userDao.dao?method=query");
           }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //??????????????????
    public void showUser(HttpServletRequest req, HttpServletResponse resp){
        String userCode = req.getParameter("uid");
        if(userCode != null && userCode.length()>0){
            UserService userService=new UserServiceImpl();
            User user = userService.showUser(userCode);
            if(req.getParameter("method") != null && req.getParameter("method").equals("modify")){
                try {
                    req.setAttribute("user",user);
                    req.getRequestDispatcher("/jsp/usermodify.jsp").forward(req,resp);
                } catch (ServletException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }else if(req.getParameter("method") != null && req.getParameter("method").equals("view")) {
                try {
                    req.setAttribute("user",user);
                    req.getRequestDispatcher("/jsp/userview.jsp").forward(req,resp);
                } catch (ServletException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }
}
