package com.rpc.core.registery;

import com.rpc.common.exception.RpcException;

import java.net.InetSocketAddress;

/**
 * 服务注册接口
 * @author 21380
 */
public interface ServiceRegistry {
    /**
     * 服务注册
     * @param serviceName 服务名称
     * @param inetSocketAddress ip+port
     */
    void register(String serviceName, InetSocketAddress inetSocketAddress) throws RpcException;

    /**
     * 服务发现
     * @param serviceName 服务名称
     * @return ip+port
     */
    InetSocketAddress lookupService(String serviceName);
}
