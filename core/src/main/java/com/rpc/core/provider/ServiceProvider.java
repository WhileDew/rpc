package com.rpc.core.provider;

import com.rpc.common.exception.RpcException;

/**
 * @author 21380
 */
public interface ServiceProvider {
    <T> void register(T service) throws RpcException;
    Object getService(String serviceName) throws RpcException;
}