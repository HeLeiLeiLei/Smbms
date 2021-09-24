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

    //添加订单
    public int addBill(Bill bill) throws SQLException;

    //显示订单数据
    public Bill showBill(int billId);

    //修改订单数据
    public int updateBill(Bill bill) throws SQLException;

    //删除订单
    public int deleteBill(int billId) throws SQLException;
}
