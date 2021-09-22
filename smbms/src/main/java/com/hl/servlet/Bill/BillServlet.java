package com.hl.servlet.Bill;


import com.hl.pojo.Bill;
import com.hl.service.bill.BillService;
import com.hl.service.bill.BillServiceImpl;
import com.hl.util.Page;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class BillServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String value=req.getParameter("method");
        if(value != null && value.equals("query")){
            this.getBillList(req,resp);
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

        System.out.println("进入到getBillList");

        BillService billService=new BillServiceImpl();
        int billCount = billService.getBillCount();
        System.out.println(billCount);
        int currentPage=1;
        int pageSize=5;
        Page page=new Page(currentPage,pageSize,billCount);
        if(cruPage != null && cruPage.length()>0){
            page.setStart(Integer.parseInt(cruPage));
            page.setCurrentPage(Integer.parseInt(cruPage));
        }
        System.out.println("-------");
        List<Bill> billList=null;
        int qyProviderId=0;
        int qyIsPayment=0;
        try {
            if(queryProviderId != null && queryIsPayment != null){
                qyProviderId=Integer.parseInt(queryProviderId);
                qyIsPayment=Integer.parseInt(queryIsPayment);
            }
            billList = billService.getBillList(queryProductName,
                    qyProviderId,
                    qyIsPayment,
                    page.getStart(),
                    page.getPageSize());
            req.setAttribute("billList",billList);
            req.setAttribute("page",page);
            req.setAttribute("providerList",billService.getProviderList());
            req.getRequestDispatcher("/jsp/billlist.jsp").forward(req,resp);
        } catch (ServletException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
