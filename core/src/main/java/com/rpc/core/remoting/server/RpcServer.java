package com.rpc.core.remoting.server;

import com.rpc.common.exception.RpcException;

/**
 * @author 21380
 */
public interface RpcServer {
    void start();
    <T> void publishService(Object service, Class<T> serviceClass) throws RpcException;
}
