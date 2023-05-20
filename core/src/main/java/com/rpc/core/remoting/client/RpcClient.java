package com.rpc.core.remoting.client;

import com.rpc.common.exception.RpcException;
import com.rpc.core.message.RpcRequest;

/**
 * @author 21380
 */
public interface RpcClient {
    /**
     * 发送rpc请求
     * @param rpcRequest rpc请求
     * @return 响应
     */
    Object sendRequest(RpcRequest rpcRequest) throws RpcException;
}
