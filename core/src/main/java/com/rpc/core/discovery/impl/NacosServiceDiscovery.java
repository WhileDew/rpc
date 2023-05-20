package com.rpc.core.discovery.impl;

import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.pojo.Instance;
import com.rpc.core.discovery.ServiceDiscovery;
import com.rpc.core.loadbalance.LoadBalancer;
import com.rpc.core.loadbalance.impl.RoundRobinLoadBalancer;
import com.rpc.core.utils.NacosUtil;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;
import java.util.List;

/**
 * nacos服务发现
 * @author 21380
 */
@Slf4j
@NoArgsConstructor
public class NacosServiceDiscovery implements ServiceDiscovery {
    /**
     * 默认为 负载均衡轮询
     */
    private static LoadBalancer loadBalancer = new RoundRobinLoadBalancer();

    public NacosServiceDiscovery(LoadBalancer loadBalancer) {
        NacosServiceDiscovery.loadBalancer = loadBalancer;
    }

    public static InetSocketAddress lookupService(String serviceName) {
        try {
            log.info("serviceName:" + serviceName);
            List<Instance> instances = NacosUtil.getAllInstance(serviceName);
            Instance instance = loadBalancer.select(instances);
            return new InetSocketAddress(instance.getIp(), instance.getPort());
        } catch (NacosException e) {
            log.error("获取服务时有错误发生:", e);
        }
        return null;
    }
}
