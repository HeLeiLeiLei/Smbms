package com.hl.servlet.Bill;


import com.alibaba.fastjson.JSONArray;
import com.hl.pojo.Bill;
import com.hl.pojo.Provider;
import com.hl.pojo.User;
import com.hl.service.bill.BillService;
import com.hl.service.bill.BillServiceImpl;
import com.hl.util.Constants;
import com.hl.util.Page;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Date;
import java.util.List;

public class BillServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String value=req.getParameter("method");
        if(value != null && value.equals("query")){
            this.getBillList(req,resp);
        }else if(value != null && value.equals("getproviderlist")){
            this.getProvidList(req,resp);
        }else if(value != null && value.equals("add")){
            this.addBill(req,resp);
        }

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }

    public void getBillList(HttpServletRequest req, HttpServletResponse resp){
        String queryProductName = req.getParameter("queryProductName");
        String queryProviderId = req.getParameter("queryProviderId");
        String queryIsPayment = req.getParameter("queryIsPayment");
        String cruPage=req.getParameter("currentPage");

        List<Bill> billList=null;

        int qyProviderId=0;
        int qyIsPayment=0;
        if(queryProviderId != null && queryIsPayment != null){
            qyProviderId=Integer.parseInt(queryProviderId);
            qyIsPayment=Integer.parseInt(queryIsPayment);
        }
        BillService billService=new BillServiceImpl();
        int billCount = billService.getBillCount(queryProductName,qyProviderId,qyIsPayment);
        int currentPage=1;
        int pageSize=5;
        Page page=new Page(currentPage,pageSize,billCount);
        if(cruPage != null && cruPage.length()>0){
            page.setStart(Integer.parseInt(cruPage));
            page.setCurrentPage(Integer.parseInt(cruPage));
        }
        try {

            billList = billService.getBillList(queryProductName,
                    qyProviderId,
                    qyIsPayment,
                    page.getStart(),
                    page.getPageSize());
            req.setAttribute("billList",billList);
            req.setAttribute("page",page);
            req.setAttribute("queryProductName",queryProductName);
            req.setAttribute("queryProviderId",queryProviderId);
            req.setAttribute("queryIsPayment",queryIsPayment);
            req.setAttribute("providerList",billService.getProviderList());
            req.getRequestDispatcher("/jsp/billlist.jsp").forward(req,resp);
        } catch (ServletException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void getProvidList(HttpServletRequest req, HttpServletResponse resp){
        BillService billService=new BillServiceImpl();
        List<Provider> providerList = billService.getProviderList();
        try {
            PrintWriter writer = resp.getWriter();
            resp.setContentType("application/json");
            writer.write(JSONArray.toJSONString(providerList));
            writer.flush();
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addBill(HttpServletRequest req, HttpServletResponse resp){
        String billCode = req.getParameter("billCode");
        String productName = req.getParameter("productName");
        String productUnit = req.getParameter("productUnit");
        String productCount = req.getParameter("productCount");
        String totalPrice = req.getParameter("totalPrice");
        String queryProviderId = req.getParameter("providerId");
        String isPayment = req.getParameter("isPayment");

        Bill bill=new Bill();
        bill.setBillCode(billCode);
        bill.setProductName(productName);
        bill.setProductUnit(productUnit);
        BigDecimal bigDecimal=new BigDecimal(productCount);
        bill.setProductCount(bigDecimal);
        BigDecimal bigDecima2=new BigDecimal(totalPrice);
        bill.setTotalPrice(bigDecima2);
        bill.setProviderId(Integer.parseInt(queryProviderId));
        bill.setIsPayment(Integer.parseInt(isPayment));
        bill.setCreatedBy(((User)req.getSession().getAttribute(Constants.USER_SESSION)).getId());
        bill.setCreationDate(new Date());

        BillService billService=new BillServiceImpl();

        try {
            int i = billService.addBill(bill);
            if(i>0){
                System.out.println("进入到if");
                resp.sendRedirect(req.getContextPath()+"/sys/Bill.dao?method=query");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
