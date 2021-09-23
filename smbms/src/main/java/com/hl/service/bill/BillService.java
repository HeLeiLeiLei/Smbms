package com.hl.service.bill;

import com.hl.pojo.Bill;
import com.hl.pojo.Provider;

import java.sql.SQLException;
import java.util.List;

public interface BillService {

    //获取所有订单信息
    public List<Bill> getBillList(String queryProductName,
                                  int queryProviderId,
                                  int isPayment,
                                  int currentPage,
                                  int pageSize);

    //获取所有供应商名称
    public List<Provider> getProviderList();

    //获取所有订单数量
    public int getBillCount(String queryProductName,
                            int queryProviderId,
                            int queryIsPayment);

    //添加用户
    public int addBill(Bill bill) throws SQLException;
}
