package com.hl.dao.bill;

import com.hl.dao.BaseDao;
import com.hl.pojo.Bill;
import com.hl.pojo.Provider;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class BillDaoImpl implements BillDao {
    PreparedStatement pstm=null;
    ResultSet rs=null;
    public List<Bill> getBillList(Connection connection,
                                  String queryProductName,
                                  int queryProviderId,
                                  int isPayment,
                                  int currentPage,
                                  int pageSize) {
        StringBuffer sql=new StringBuffer();
        List<Bill> billList=new ArrayList<Bill>();
        if(connection != null){
            List list=new ArrayList();
            sql.append("select b.billCode,b.productName,p.proName,b.totalPrice,b.isPayment,b.creationDate\n" +
                    "from smbms_bill b,smbms_provider p where b.providerid=p.id");
            if(queryProductName != null && queryProductName.length()>0){
                sql.append(" and productName like ?");
                list.add("%"+queryProductName+"%");
            }
            if(queryProviderId>0){
                sql.append(" and b.providerId=?");
                list.add(queryProviderId);
            }
            if(isPayment>0){
                sql.append(" and b.isPayment=?");
                list.add(isPayment);
            }
            sql.append(" order by b.id asc limit ?,?");
            list.add(currentPage);
            list.add(pageSize);

            //将list转换为数组
            Object[] array = list.toArray();
            try {
                rs = BaseDao.executQ(connection,pstm,rs,array,sql.toString());
                while (rs.next()){
                    Bill bill=new Bill();
                    bill.setBillCode(rs.getString("billCode"));
                    bill.setProductName(rs.getString("productName"));
                    bill.setProviderName(rs.getString("proName"));
                    bill.setTotalPrice(rs.getBigDecimal("totalPrice"));
                    bill.setIsPayment(rs.getInt("isPayment"));
                    bill.setCreationDate(rs.getDate("creationDate"));
                    billList.add(bill);
               }
            } catch (Exception e) {
                e.printStackTrace();
            }finally {
                BaseDao.closeResource(null,pstm,rs);
            }
        }
        return billList;
    }

    public List<Provider> getProviderList(Connection connection) {
        List<Provider> list=new ArrayList<Provider>();
        if(connection != null){
            String sql="select * from smbms_provider";
            try {
                rs=BaseDao.executQ(connection,pstm,rs,null,sql);
                while (rs.next()){
                    Provider provider=new Provider();
                    provider.setId(rs.getInt("id"));
                    provider.setProName(rs.getString("proName"));
                    list.add(provider);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }finally {
                BaseDao.closeResource(null,pstm,rs);
            }
        }
        return list;
    }

    public int getBillCount(Connection connection,
                            String queryProductName,
                            int queryProviderId,
                            int queryIsPayment) {
        int count=0;
        if(connection != null){
            StringBuffer sql=new StringBuffer();
            sql.append("select count(1) as count from smbms_bill where 1=1");
            ArrayList<Object> list = new ArrayList<Object>();
            if(queryProductName != null && queryProductName.length()>0){
                sql.append(" and productName like ?");
                list.add("%"+queryProductName+"%");
            }
            if(queryProviderId > 0){
                sql.append(" and providerId=?");
                list.add(queryProviderId);
            }
            if(queryIsPayment > 0 ){
                sql.append(" and isPayment=?");
                list.add(queryIsPayment);
            }
            Object[] array = list.toArray();
            try {
                rs=BaseDao.executQ(connection,pstm,rs,array,sql.toString());
                while (rs.next()){
                    count=rs.getInt("count");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }finally {
                BaseDao.closeResource(null,pstm,rs);
            }
        }
        return count;
    }

    public int addBill(Connection connection, Bill bill) {
        int num=0;
        if(connection != null){
            String sql="insert into smbms_bill(billCode,productName," +
                    "productUnit,productCount,totalPrice,providerId,isPayment,createdBy,creationDate)" +
                    " values(?,?,?,?,?,?,?,?,?)";
            List list=new ArrayList();
            list.add(bill.getBillCode());
            list.add(bill.getProductName());
            list.add(bill.getProductUnit());
            list.add(bill.getProductCount());
            list.add(bill.getTotalPrice());
            list.add(bill.getProviderId());
            list.add(bill.getIsPayment());
            list.add(bill.getCreatedBy());
            list.add(bill.getCreationDate());

            Object parms[] = list.toArray();
            try {
                num = BaseDao.executU(connection, pstm, parms, sql);
            } catch (Exception e) {
                e.printStackTrace();
            }finally {
                BaseDao.closeResource(null,pstm,null);
            }


        }
        return num;
    }
}
