package com.rpc.common.exception;

/**
 * 自定义RPC异常
 * @author 21380
 */
public class RpcException extends Exception{
    public RpcException(String msg) {
        super(msg);
    }
}
