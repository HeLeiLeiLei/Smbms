package com.hl.service.provider;

import com.hl.pojo.Provider;

import java.sql.SQLException;
import java.util.List;

public interface ProviderService {
    //获取所有供应商信息
    public List<Provider> getProviderList(String proCode,String proName,int cruntPage,int pageSize);

    //查询所有供应商的数量
    public int findProviderCount(String proCode,String proName);

    //添加供应商
    public int addProvider(Provider provider) throws SQLException;

    //删除供应商
    public int deleteProvider(int providerId) throws SQLException;

    //显示供应商信息
    public Provider showProvide(int providerId) throws SQLException;

    //修改供应商信息
    public int updateProvider(Provider provider) throws SQLException;
}
