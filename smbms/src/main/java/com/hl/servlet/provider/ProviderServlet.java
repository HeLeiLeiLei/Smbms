package com.hl.servlet.provider;

import com.alibaba.fastjson.JSONArray;
import com.hl.pojo.Provider;
import com.hl.pojo.User;
import com.hl.service.provider.ProviderService;
import com.hl.service.provider.ProviderServiceImpl;
import com.hl.util.Constants;
import com.hl.util.PageSupport;
import com.hl.util.PageUtils;

import javax.management.StringValueExp;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProviderServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {


        if (req.getParameter("method") != null && req.getParameter("method").equals("query")) {
            this.getProviderList(req, resp);
        } else if (req.getParameter("method") != null && req.getParameter("method").equals("add")) {
            this.addProvider(req, resp);
        } else if (req.getParameter("method") != null && req.getParameter("method").equals("delprovider")) {
            this.deleteProvider(req, resp);
        } else if (req.getParameter("method") != null && req.getParameter("method").equals("modify")) {
            this.shouProvider(req,resp);
        }else if(req.getParameter("method") != null && req.getParameter("method").equals("updateProvider")){
            this.updateProvider(req,resp);
        }else if(req.getParameter("method") != null && req.getParameter("method").equals("view")){
            this.shouProvider(req,resp);
        }

    }



    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }

    //获取所有供应商的信息
    public void getProviderList(HttpServletRequest req, HttpServletResponse resp){

        //获取前端传过来的参数
        String queryProCode = req.getParameter("queryProCode");
        String queryProName=req.getParameter("queryProName");
        String pageIndex=req.getParameter("page");

        int currentPage=0;
        int pageSize=5;
        //通过findProviderCount()方法获取总数 并且设置分页中的数据
        ProviderService providerService =new ProviderServiceImpl();
        int totalCount = providerService.findProviderCount(queryProCode, queryProName);
        //执行获取供应商的所有数据
        try{
            List<Provider> providerList = providerService.getProviderList(queryProCode, queryProName,currentPage, pageSize);
            req.setAttribute("queryProCode",queryProCode);
            req.setAttribute("queryProName",queryProName);
            req.setAttribute("providerList",providerList);

            req.getRequestDispatcher("/jsp/providerlist.jsp").forward(req,resp);
        } catch (ServletException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //添加供应商
    public void addProvider(HttpServletRequest req, HttpServletResponse resp){

        String proCode = req.getParameter("proCode");
        String proName = req.getParameter("proName");
        String proContact = req.getParameter("proContact");
        String proPhone = req.getParameter("proPhone");
        String proAddress = req.getParameter("proAddress");
        String proFax = req.getParameter("proFax");
        String proDesc = req.getParameter("proDesc");

        Provider provider=new Provider();
        provider.setProCode(proCode);
        provider.setProName(proName);
        provider.setProContact(proContact);
        provider.setProPhone(proPhone);
        provider.setProAddress(proAddress);
        provider.setProFax(proFax);
        provider.setProDesc(proDesc);
        provider.setCreatedBy(((User)req.getSession().getAttribute(Constants.USER_SESSION)).getId());
        provider.setCreationDate(new Date());

        ProviderService providerService=new ProviderServiceImpl();
        try {
            int num=providerService.addProvider(provider);
            if(num>0){
                resp.sendRedirect(req.getContextPath()+"/sys/Provider.dao?method=query");
                //req.getRequestDispatcher("/sys/Provider.dao?method=query").forward(req,resp);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    //删除供应商
    public void deleteProvider(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String proid = req.getParameter("proid");
        Map<String, String> hashMap = new HashMap<String, String>();
        if(proid != null && proid.length()>0){
            ProviderService providerService=new ProviderServiceImpl();
            try {
                int i = providerService.deleteProvider(Integer.parseInt(proid));
                if(i>0){
                    hashMap.put("delResult","true");
                }else {
                    hashMap.put("delResult","false");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }else {
            hashMap.put("delResult","notexist");
        }
        resp.setContentType("application/json");
        PrintWriter writer = resp.getWriter();
        writer.write(JSONArray.toJSONString(hashMap));
        writer.flush();
        writer.close();
    }

    //显示供应商信息
    public void shouProvider(HttpServletRequest req, HttpServletResponse resp){
        String providerId=req.getParameter("proid");
        if(providerId != null && providerId.length()>0){
            try {
                ProviderService providerService=new ProviderServiceImpl();
                Provider provider = providerService.showProvide(Integer.parseInt(providerId));
                if(req.getParameter("method").equals("modify")){
                    req.setAttribute("provider",provider);
                    req.getRequestDispatcher("/jsp/providermodify.jsp").forward(req,resp);
                }else if (req.getParameter("method").equals("view")){
                    req.setAttribute("provider",provider);
                    req.getRequestDispatcher("/jsp/providerview.jsp").forward(req,resp);
                }
            } catch (ServletException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    //修改供应商信息
    public void updateProvider(HttpServletRequest req, HttpServletResponse resp){
        String uid = req.getParameter("uid");
        String proCode = req.getParameter("proCode");
        String proName = req.getParameter("proName");
        String proContact = req.getParameter("proContact");
        String proPhone = req.getParameter("proPhone");
        String proAddress = req.getParameter("proAddress");
        String proFax = req.getParameter("proFax");
        String proDesc = req.getParameter("proDesc");
        if(uid != null && uid.length()>0){
            Provider provider=new Provider();
            provider.setId(Integer.parseInt(uid));
            provider.setProCode(proCode);
            provider.setProName(proName);
            provider.setProContact(proContact);
            provider.setProPhone(proPhone);
            provider.setProAddress(proAddress);
            provider.setProFax(proFax);
            provider.setProDesc(proDesc);
            provider.setModifyBy(((User)req.getSession().getAttribute(Constants.USER_SESSION)).getId());
            provider.setModifyDate(new Date());
            ProviderService providerService=new ProviderServiceImpl();
            try {
                int i=providerService.updateProvider(provider);
                if(i>0){
                    resp.sendRedirect(req.getContextPath()+"/sys/Provider.dao?method=query");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
