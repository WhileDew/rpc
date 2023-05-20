package com.rpc.core.loadbalance.impl;

import com.alibaba.nacos.api.naming.pojo.Instance;
import com.rpc.core.loadbalance.LoadBalancer;
import com.rpc.core.message.RpcRequest;

import java.util.List;

/**
 * 轮询负载均衡
 * @author 21380
 */
public class RoundRobinLoadBalancer implements LoadBalancer {
    private int index = 0;

    @Override
    public Instance select(List<Instance> instances) {
        System.out.println(instances);
        if (index % instances.size() == 0) {
            return instances.get(index++);
        }
        return instances.get(index++ % instances.size());
    }
}
