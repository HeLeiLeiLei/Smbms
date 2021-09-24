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
            sql.append("select b.billCode,b.id,b.productName,p.proName,b.totalPrice,b.isPayment,b.creationDate\n" +
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
                    bill.setId(rs.getInt("id"));
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

    public Bill showBill(Connection connection, int BillId) {

        Bill bill=new Bill();
        if(connection != null){
            String sql="select p.proName,b.* from smbms_provider p,smbms_bill b where b.providerid=p.id and b.id=?;";
            Object parms[]={BillId};
            try {
                rs = BaseDao.executQ(connection, pstm, rs, parms, sql);
                while (rs.next()){
                    bill.setId(rs.getInt("id"));
                    bill.setBillCode(rs.getString("billCode"));
                    bill.setProductName(rs.getString("productName"));
                    bill.setProductUnit(rs.getString("productUnit"));
                    bill.setProductCount(rs.getBigDecimal("productCount"));
                    bill.setTotalPrice(rs.getBigDecimal("totalPrice"));
                    bill.setProviderName(rs.getString("proName"));
                    bill.setIsPayment(rs.getInt("isPayment"));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }finally {
                BaseDao.closeResource(null,pstm,rs);
            }
        }

        return bill;
    }

    public int updateBill(Connection connection, Bill bill) {
        int i=0;
        if(connection != null){
            String sql="update smbms_bill set billCode=?,productName=?" +
                    ",productUnit=?,productCount=?,totalPrice=?,providerId=?,isPayment=?,modifyBy=?,modifyDate=? where id=?";
            ArrayList<Object> arrayList = new ArrayList<Object>();
            arrayList.add(bill.getBillCode());
            arrayList.add(bill.getProductName());
            arrayList.add(bill.getProductUnit());
            arrayList.add(bill.getProductCount());
            arrayList.add(bill.getTotalPrice());
            arrayList.add(bill.getProviderId());
            arrayList.add(bill.getIsPayment());
            arrayList.add(bill.getModifyBy());
            arrayList.add(bill.getModifyDate());
            arrayList.add(bill.getId());
            Object[] array = arrayList.toArray();
            try {
                i=BaseDao.executU(connection,pstm,array,sql);
            } catch (Exception e) {
                e.printStackTrace();
            }finally {
                BaseDao.closeResource(null,pstm,null);
            }
        }
        return i;
    }

    public int deleteBill(Connection connection, int BillId) {
        int num=0;
        if(connection != null){
            String sql="delete from smbms_bill where id=?";
            Object prams[]={BillId};
            try {
                num = BaseDao.executU(connection, pstm, prams, sql);
            } catch (Exception e) {
                e.printStackTrace();
            }finally {
                BaseDao.closeResource(null,pstm,null);
            }
        }
        return num;
    }
}
