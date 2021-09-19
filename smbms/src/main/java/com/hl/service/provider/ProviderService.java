package com.hl.service.provider;

import com.hl.pojo.Provider;

import java.util.List;

public interface ProviderService {
    //获取所有供应商信息
    public List<Provider> getProviderList(String proCode,String proName,int cruntPage,int pageSize);

    //查询所有供应商的数量
    public int findProviderCount(String proCode,String proName);
}
