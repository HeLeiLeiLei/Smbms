package com.hl.dao.provider;

import com.hl.pojo.Provider;

import java.sql.Connection;
import java.util.List;

public interface ProviderDao {
    //查询所有供应商信息
    public List<Provider> getProviderList(Connection connection,
                                          String queryProCode,
                                          String queryProName,
                                          int cruntPage,
                                          int pageSize);
    //查询供应商数量
    public int findProviderCount(Connection connection,String proCode,String proName);
}
