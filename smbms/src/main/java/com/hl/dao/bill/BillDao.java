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
    public int getBillCount(Connection connection,
                            String queryProductName,
                            int queryProviderId,
                            int queryIsPayment);

    //添加订单
    public int addBill(Connection connection,Bill bill);

    //显示订单信息
    public Bill showBill(Connection connection,int BillId);

    //修改订单信息
    public int updateBill(Connection connection,Bill bill);

    //删除订单
    public int deleteBill(Connection connection,int BillId);


}
