package com.rpc.core.proxy;

import com.rpc.core.remoting.client.RpcClient;
import com.rpc.core.message.RpcRequest;
import java.lang.reflect.Proxy;

/**
 * @author 21380
 */
public class RpcClientProxy {
    private final RpcClient rpcClient;
    public RpcClientProxy(RpcClient rpcClient) {
        this.rpcClient = rpcClient;
    }

    @SuppressWarnings("unchecked")
    public <T> T getProxy(Class<T> clazz) {
        return (T) Proxy.newProxyInstance(clazz.getClassLoader(), new Class<?>[]{clazz}, (proxy, method, args) -> {
            RpcRequest rpcRequest = RpcRequest.builder()
                    .interfaceName(method.getDeclaringClass().getName())
                    .methodName(method.getName())
                    .parameters(args)
                    .paramTypes(method.getParameterTypes())
                    .build();
            return rpcClient.sendRequest(rpcRequest);
        });
    }

}