package com.rpc.core.registery.impl;


import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingFactory;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.pojo.Instance;
import com.rpc.common.constants.RpcError;
import com.rpc.common.exception.RpcException;
import com.rpc.core.discovery.impl.NacosServiceDiscovery;
import com.rpc.core.registery.ServiceRegistry;
import com.rpc.core.utils.NacosUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.comparator.Comparators;

import java.net.InetSocketAddress;
import java.util.List;

/**
 * @author 21380
 */
@Slf4j
public class NacosServiceRegistry implements ServiceRegistry {

    @Override
    public void register(String serviceName, InetSocketAddress inetSocketAddress) throws RpcException {
        NacosUtil.register(serviceName, inetSocketAddress);
    }

    @Override
    public InetSocketAddress lookupService(String serviceName) {
        return NacosServiceDiscovery.lookupService(serviceName);
    }
}

