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

    //添加供应商
    public int addPrvider(Connection connection,Provider provider);

    //删除供应商
    public int deleteProvider(Connection connection,int ProviderId);

    //显示供应商详细信息
    public Provider showProvider(Connection connection,int ProviderId);
}
