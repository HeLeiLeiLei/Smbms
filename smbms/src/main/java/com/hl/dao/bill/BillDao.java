package com.hl.dao.bill;

import com.hl.pojo.Bill;
import com.hl.pojo.Provider;

import java.sql.Connection;
import java.util.List;

public interface BillDao {
    //获取所有订单信息
    public List<Bill> getBillList(Connection connection,String queryProductName,
                                  int queryProviderId,int isPayment,int currentPage,
                                  int pageSize);
    //获取所有供应商的信息
    public List<Provider> getProviderList(Connection connection);

    //获取所有订单数量
    public int getBillCount(Connection connection);
}
