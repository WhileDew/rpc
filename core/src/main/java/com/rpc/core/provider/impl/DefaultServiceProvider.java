package com.rpc.core.provider.impl;

import com.rpc.common.constants.RpcError;
import com.rpc.common.exception.RpcException;
import com.rpc.core.provider.ServiceProvider;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 服务注册实现类
 * @author 21380
 */
@Slf4j
public class DefaultServiceProvider implements ServiceProvider {

    /**
     * key：规范接口类名，value：服务实现类名
     */
    private static final Map<String, Object> SERVICE_MAP = new ConcurrentHashMap<>();
    private static final Set<String> REGISTERED_SERVICE = ConcurrentHashMap.newKeySet();

    @Override
    public synchronized <T> void register(T service) throws RpcException {
        // 获取Java规范类名
        String serviceName = service.getClass().getCanonicalName();
        log.info("有服务被注册进来:" + serviceName);
        // 如果map中已经存在返回
        if(REGISTERED_SERVICE.contains(serviceName)) {
            return;
        }
        // 存入服务集合中
        REGISTERED_SERVICE.add(serviceName);
        // 获取服务实现的接口
        Class<?>[] interfaces = service.getClass().getInterfaces();
        if(interfaces.length == 0) {
            throw new RpcException(RpcError.SERVICE_NOT_IMPLEMENT_ANY_INTERFACE);
        }
        // 存入map中
        for(Class<?> i : interfaces) {
            SERVICE_MAP.put(i.getCanonicalName(), service);
        }
        log.info("向接口: {} 注册服务: {}", interfaces, serviceName);
        log.info("注册成功" + SERVICE_MAP);
    }

    @Override
    public synchronized Object getService(String serviceName) throws RpcException {
        log.info("获取前测试能不能获取到map:"+SERVICE_MAP);
        log.info("serviceName:"+serviceName);
        // 从serviceMap中获取服务的实现类
        Object service = SERVICE_MAP.get(serviceName);
        if(service == null) {
            throw new RpcException(RpcError.SERVICE_NOT_FOUND);
        }
        return service;
    }
}