package com.rpc.server.service;

import com.rpc.core.annotation.RPCService;

@RPCService
public class HelloServiceImpl implements HelloService{
    @Override
    public String hello(String msg) {
        return msg;
    }
}
