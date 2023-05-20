package com.rpc.core.remoting.server;

import com.rpc.common.constants.RpcError;
import com.rpc.common.exception.RpcException;
import com.rpc.core.annotation.RPCService;
import com.rpc.core.annotation.RPCServiceScan;
import com.rpc.core.codec.CommonDecoder;
import com.rpc.core.codec.CommonEncoder;
import com.rpc.core.provider.ServiceProvider;
import com.rpc.core.provider.impl.DefaultServiceProvider;
import com.rpc.core.registery.ServiceRegistry;
import com.rpc.core.registery.impl.NacosServiceRegistry;
import com.rpc.core.remoting.handler.NettyServerHandler;
import com.rpc.core.serializer.impl.HessianSerializer;
import com.rpc.core.serializer.impl.KryoSerializer;
import com.rpc.core.utils.ReflectUtil;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;
import java.util.Set;

/**
 * @author 21380
 */
@Slf4j
public class NettyServer implements RpcServer {

    private final String host;
    private final int port;
    private final ServiceRegistry serviceRegistry;
    private final ServiceProvider serviceProvider;
    public NettyServer(String host, int port) {
        this.host = host;
        this.port = port;
        serviceRegistry = new NacosServiceRegistry();
        serviceProvider = new DefaultServiceProvider();
        // 自动扫描服务
        scanServices();
    }

    @Override
    public void start() {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .option(ChannelOption.SO_BACKLOG, 256)
                    .option(ChannelOption.SO_KEEPALIVE, true)
                    .childOption(ChannelOption.TCP_NODELAY, true)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) {
                            ChannelPipeline pipeline = ch.pipeline();
                            // 使用kryo作为默认序列化器
                            pipeline.addLast(new CommonEncoder(new KryoSerializer()));
                            // pipeline.addLast(new CommonEncoder(new HessianSerializer()));
                            pipeline.addLast(new CommonDecoder());
                            pipeline.addLast(new NettyServerHandler());
                        }
                    });
            ChannelFuture future = serverBootstrap.bind(port).sync();
            future.channel().closeFuture().addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture future) throws Exception {

                }
            }).sync();

        } catch (InterruptedException e) {
            log.error("启动服务器时有错误发生: ", e);
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

    /**
     * 发布服务
     */
    public <T> void publishService(Object service, Class<T> serviceClass) throws RpcException {
        serviceProvider.register(service);
        serviceRegistry.register(service.getClass().getInterfaces()[0].getName(), new InetSocketAddress(host, port));
        this.start();
    }

    public void scanServices() {
        String mainClassName = ReflectUtil.getStackTrace();
        Class<?> startClass;
        try {
            startClass = Class.forName(mainClassName);
            if(!startClass.isAnnotationPresent(RPCServiceScan.class)) {
                log.error("启动类缺少 @ServiceScan 注解");
                try {
                    throw new RpcException(RpcError.SERVICE_SCAN_PACKAGE_NOT_FOUND);
                } catch (RpcException e) {
                    throw new RuntimeException(e);
                }
            }
        } catch (ClassNotFoundException e) {
            log.error("出现未知错误");
            try {
                throw new RpcException(RpcError.UNKNOWN_ERROR);
            } catch (RpcException ex) {
                throw new RuntimeException(ex);
            }
        }
        String basePackage = startClass.getAnnotation(RPCServiceScan.class).value();
        if("".equals(basePackage)) {
            basePackage = mainClassName.substring(0, mainClassName.lastIndexOf("."));
        }
        Set<Class<?>> classSet = ReflectUtil.getClasses(basePackage);
        for(Class<?> clazz : classSet) {
            // 存在RPCService注解
            if(clazz.isAnnotationPresent(RPCService.class)) {
                Object obj;
                try {
                    obj = clazz.newInstance();
                } catch (InstantiationException | IllegalAccessException e) {
                    log.error("创建 " + clazz + " 时有错误发生");
                    continue;
                }
                try {
                    log.info("obj.getClass().getInterfaces():" + obj.getClass().getInterfaces()[0].getName());
                    log.info("clazz:" + clazz);
                    publishService(obj, clazz);
                } catch (RpcException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}
