package com.hl.service.provider;

import com.hl.dao.BaseDao;
import com.hl.dao.provider.ProviderDao;
import com.hl.dao.provider.ProviderDaoImpl;
import com.hl.pojo.Provider;
import org.junit.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class ProviderServiceImpl implements ProviderService{
    private ProviderDao providerDao;
    public ProviderServiceImpl(){
        providerDao=new ProviderDaoImpl();
    }

    public List<Provider> getProviderList(String proCode, String proName, int cruntPage, int pageSize) {
        Connection connection=null;
        List<Provider> providerList=null;
        try {
            connection= BaseDao.getConnection();
            providerList = providerDao.getProviderList(connection, proCode, proName, cruntPage, pageSize);
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            BaseDao.closeResource(connection,null,null);
        }
        return providerList;
    }

    public int findProviderCount(String proCode,String proName) {
        Connection connection=null;
        int providerCount= 0;
        try {
            connection=BaseDao.getConnection();
            providerCount = providerDao.findProviderCount(connection,proCode,proName);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return providerCount;
    }

    public int addProvider(Provider provider) throws SQLException {
        int num=0;
        Connection connection=null;
        try {
            connection=BaseDao.getConnection();
            connection.setAutoCommit(false);//开启事务
            num=providerDao.addPrvider(connection,provider);
            connection.commit();//提交事务
        } catch (Exception e) {
            e.printStackTrace();
            connection.rollback();//业务回滚
        }finally {
            BaseDao.closeResource(connection,null,null);
        }
        return num;
    }

    public int deleteProvider(int providerId) throws SQLException {
        Connection connection=null;
        int num=0;
        try {
            connection=BaseDao.getConnection();
            connection.setAutoCommit(false);
            num=providerDao.deleteProvider(connection,providerId);
            connection.commit();
        } catch (SQLException e) {
            e.printStackTrace();
            connection.rollback();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            BaseDao.closeResource(connection,null,null);
        }
        return num;
    }

    public Provider showProvide(int providerId) throws SQLException {
        Connection connection=null;
        Provider provider=new Provider();
        try {
            connection = BaseDao.getConnection();
            connection.setAutoCommit(false);
            provider=providerDao.showProvider(connection,providerId);
            connection.commit();
        } catch (Exception e) {
            e.printStackTrace();
            connection.rollback();
        }finally {
            BaseDao.closeResource(connection,null,null);
        }
        return provider;
    }

    @Test
    public void Test(){
        ProviderServiceImpl providerService=new ProviderServiceImpl();
        try{
            Provider provider = providerService.showProvide(2);
            if(provider != null){
                System.out.println("succes");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
