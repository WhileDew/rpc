package com.rpc.core.loadbalance;

import com.alibaba.nacos.api.naming.pojo.Instance;

import java.util.List;

/**
 * @author 21380
 */
public interface LoadBalancer {
    /**
     * 选择实例
     * @param instances 实例列表
     * @return 实例
     */
    Instance select(List<Instance> instances);
}