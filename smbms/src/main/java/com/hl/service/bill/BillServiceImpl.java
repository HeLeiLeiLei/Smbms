package com.hl.service.bill;

import com.hl.dao.BaseDao;
import com.hl.dao.bill.BillDao;
import com.hl.dao.bill.BillDaoImpl;
import com.hl.pojo.Bill;
import com.hl.pojo.Provider;
import org.junit.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class BillServiceImpl implements BillService {

    private BillDao billDao;
    public BillServiceImpl(){
        billDao=new BillDaoImpl();
    }

    public List<Bill> getBillList(String queryProductName, int queryProviderId, int isPayment, int currentPage, int pageSize) {
        Connection connection=null;
        List<Bill> billList=null;
        try {
            connection=BaseDao.getConnection();
            billList=billDao.getBillList(connection,queryProductName,queryProviderId,isPayment,currentPage,pageSize);
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            BaseDao.closeResource(connection,null,null);
        }
        return billList;
    }

    public List<Provider> getProviderList() {
        Connection connection=null;
        List<Provider> providerList=null;
        try {
            connection=BaseDao.getConnection();
            providerList= billDao.getProviderList(connection);
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            BaseDao.closeResource(connection,null,null);
        }
        return providerList;
    }

    public int getBillCount(String queryProductName,
                            int queryProviderId,
                            int queryIsPayment) {
        int count=0;
        Connection connection=null;
        try {
            connection=BaseDao.getConnection();
            count=billDao.getBillCount(connection,queryProductName,queryProviderId,queryIsPayment);
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            BaseDao.closeResource(connection,null,null);
        }
        return count;
    }

    public int addBill(Bill bill) throws SQLException {
        Connection connection=null;
        int num=0;
        try {
            connection=BaseDao.getConnection();
            connection.setAutoCommit(false);
            num = billDao.addBill(connection, bill);
            connection.commit();
        } catch (Exception e) {
            e.printStackTrace();
            connection.rollback();
        }
        return num;
    }
}
