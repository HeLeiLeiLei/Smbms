package com.hl.dao.provider;

import com.hl.dao.BaseDao;
import com.hl.pojo.Provider;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class ProviderDaoImpl implements ProviderDao {
    PreparedStatement pstm=null;
    ResultSet rs=null;

    public List<Provider> getProviderList(Connection connection,
                                          String proCode,
                                          String proName,
                                          int cruntPage,
                                          int pageSize) {

        StringBuffer sql=new StringBuffer();
        List<Provider> providerList=new ArrayList<Provider>();
        if(connection != null){
            sql.append("select * from smbms_provider where 1=1");
            ArrayList<Object> arrayList = new ArrayList<Object>();
            if(proCode != null && proCode.length()>0){
                sql.append(" and proCode like ?");
                arrayList.add("%"+proCode+"%");
            }
            if(proName != null && proName.length()>0){
                sql.append(" and proName like ?");
                arrayList.add("%"+proName+"%");
            }
            sql.append(" order by id asc limit ?,?");
            arrayList.add(cruntPage);
            arrayList.add(pageSize);
            Object pamars[]=arrayList.toArray();
            try {
                rs = BaseDao.executQ(connection, pstm, rs, pamars, sql.toString());
                while (rs.next()){
                    Provider provider=new Provider();
                    provider.setId(rs.getInt("id"));
                    provider.setProCode(rs.getString("proCode"));
                    provider.setProName(rs.getString("proName"));
                    provider.setProContact(rs.getString("proContact"));
                    provider.setProPhone(rs.getString("proPhone"));
                    provider.setProFax(rs.getString("proFax"));
                    provider.setCreationDate(rs.getDate("creationDate"));
                    providerList.add(provider);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }finally {
                BaseDao.closeResource(null,pstm,rs);
            }
        }
        return providerList;
    }

    public int findProviderCount(Connection connection,String proCode,String proName) {
        int count=0;
        StringBuffer sql=new StringBuffer();
        ArrayList<Object> arrayList = new ArrayList<Object>();
        if(connection != null){
            sql.append("select count(1) as count from smbms_provider where 1=1");
            if(proCode != null && proCode.length()>0){
                sql.append(" and proCode like ?");
                arrayList.add("%"+proCode+"%");
            }
            if(proName != null && proName.length()>0){
                sql.append(" and proName like ?");
                arrayList.add("%"+proName+"%");
            }
            Object[] pamars = arrayList.toArray();
            try {
                rs=BaseDao.executQ(connection,pstm,rs,pamars,sql.toString());
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

    public int addPrvider(Connection connection, Provider provider) {
        int num=0;
        if(connection != null){
            String sql="insert into smbms_provider(proCode,proName,proContact,proPhone" +
                    ",proAddress,proFax,proDesc,createdBy,creationDate) values(?,?,?,?,?,?,?,?,?)";
            List<Object> arrayList = new ArrayList<Object>();
            arrayList.add(provider.getProCode());
            arrayList.add(provider.getProName());
            arrayList.add(provider.getProContact());
            arrayList.add(provider.getProPhone());
            arrayList.add(provider.getProAddress());
            arrayList.add(provider.getProFax());
            arrayList.add(provider.getProDesc());
            arrayList.add(provider.getCreatedBy());
            arrayList.add(provider.getCreationDate());

            Object prmars[] = arrayList.toArray();
            try{
                num= BaseDao.executU(connection,pstm,prmars,sql);
            } catch (Exception e) {
                e.printStackTrace();
            }finally {
                BaseDao.closeResource(null,pstm,null);
            }
        }
        return num;
    }

    public int deleteProvider(Connection connection, int ProviderId) {
        int num=0;
        if(connection != null){
            String sql="delete from smbms_provider where id=?";
            Object prmars[]={ProviderId};
            try {
                num = BaseDao.executU(connection, pstm, prmars, sql);
            } catch (Exception e) {
                e.printStackTrace();
            }finally {
                BaseDao.closeResource(null,pstm,null);
            }
        }
        return num;
    }

    public Provider showProvider(Connection connection, int ProviderId) {
        Provider provider=new Provider();
        if(connection != null){
            try {
                String sql="select * from smbms_provider where id=?";
                Object prmars[]={ProviderId};
                rs = BaseDao.executQ(connection, pstm, rs, prmars, sql);
                while (rs.next()){
                    provider.setId(rs.getInt("id"));
                    provider.setProCode(rs.getString("proCode"));
                    provider.setProName(rs.getString("proName"));
                    provider.setProContact(rs.getString("proContact"));
                    provider.setProPhone(rs.getString("proPhone"));
                    provider.setProAddress(rs.getString("proAddress"));
                    provider.setProFax(rs.getString("proFax"));
                    provider.setProDesc(rs.getString("proDesc"));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }finally {
                BaseDao.closeResource(null,pstm,rs);
            }
        }
        return provider;
    }

    public int updateProvider(Connection connection, Provider provider) {
        int num=0;
        if(connection != null){
            String sql="update smbms_provider set proCode=?,proName=?,proContact=?," +
                    "proPhone=?,proAddress=?,proFax=?,proDesc=?,modifyBy=?,modifyDate=? where id=?";
            ArrayList<Object> arrayList = new ArrayList<Object>();
            arrayList.add(provider.getProCode());
            arrayList.add(provider.getProName());
            arrayList.add(provider.getProContact());
            arrayList.add(provider.getProPhone());
            arrayList.add(provider.getProAddress());
            arrayList.add(provider.getProFax());
            arrayList.add(provider.getProDesc());
            arrayList.add(provider.getModifyBy());
            arrayList.add(provider.getModifyDate());
            arrayList.add(provider.getId());
            Object[] array = arrayList.toArray();
            try {
                num = BaseDao.executU(connection, pstm, array, sql);
            } catch (Exception e) {
                e.printStackTrace();
            }finally {
                BaseDao.closeResource(null,pstm,null);
            }

        }
        return num;
    }


}
