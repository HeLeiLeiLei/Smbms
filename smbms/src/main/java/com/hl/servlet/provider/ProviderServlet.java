package com.hl.servlet.provider;

import com.hl.pojo.Provider;
import com.hl.service.provider.ProviderService;
import com.hl.service.provider.ProviderServiceImpl;
import com.hl.util.PageSupport;
import com.hl.util.PageUtils;

import javax.management.StringValueExp;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class ProviderServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        if(req.getParameter("method") != null && req.getParameter("method").equals("query")){
            this.getProviderList(req,resp);
        }

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }

    public void getProviderList(HttpServletRequest req, HttpServletResponse resp){

        //获取前端传过来的参数
        String queryProCode = req.getParameter("queryProCode");
        String queryProName=req.getParameter("queryProName");
        String pageIndex=req.getParameter("page");


        PageUtils pageUtils=new PageUtils();
        int page=0;
        if(pageIndex != null){
            page=Integer.parseInt(pageIndex);
            if(page > 0){
                pageUtils.setPage(page);
            }
        }
        int pageSize=5;
        pageUtils.setPagesize(pageSize);
        //通过findProviderCount()方法获取总数 并且设置分页中的数据
        ProviderService providerService =new ProviderServiceImpl();
        int totalCount = providerService.findProviderCount(queryProCode, queryProName);
        pageUtils.setCount(totalCount);


        //执行获取供应商的所有数据
        try{
            System.out.println(pageUtils.getPage());
            List<Provider> providerList = providerService.getProviderList(queryProCode, queryProName, pageUtils.getPage(), pageUtils.getPagesize());
            req.setAttribute("queryProCode",queryProCode);
            req.setAttribute("queryProName",queryProName);
            req.setAttribute("pageBean",pageUtils);
            req.setAttribute("providerList",providerList);

            req.getRequestDispatcher("/jsp/providerlist.jsp").forward(req,resp);
        } catch (ServletException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
