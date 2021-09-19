package com.hl.service.provider;

import com.hl.dao.BaseDao;
import com.hl.dao.provider.ProviderDao;
import com.hl.dao.provider.ProviderDaoImpl;
import com.hl.pojo.Provider;
import org.junit.Test;

import java.sql.Connection;
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


}
