package com.rpc.core.utils;

import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingFactory;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.pojo.Instance;
import com.rpc.common.constants.RpcError;
import com.rpc.common.exception.RpcException;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;
import java.util.List;

/**
 * nacos工具类
 * @author 21380
 */
@Slf4j
public class NacosUtil {

    private static final String SERVER_ADDR = "127.0.0.1:8848";
    private static final NamingService namingService;

    static {
        try {
            namingService = NamingFactory.createNamingService(SERVER_ADDR);
        } catch (NacosException e) {
            log.error("连接到Nacos时有错误发生: ", e);
            try {
                throw new RpcException(RpcError.FAILED_TO_CONNECT_TO_SERVICE_REGISTRY);
            } catch (RpcException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    public static void register(String serviceName, InetSocketAddress inetSocketAddress) throws RpcException {
        try {
            // 通过namingService注册服务实例
            namingService.registerInstance(serviceName, inetSocketAddress.getHostName(), inetSocketAddress.getPort());
        } catch (NacosException e) {
            log.error("注册服务时有错误发生:", e);
            throw new RpcException(RpcError.REGISTER_SERVICE_FAILED);
        }
    }

    public static List<Instance> getAllInstance(String service) throws NacosException {
        return namingService.getAllInstances(service);
    }
}
