package com.rpc.core.remoting.client;

import com.rpc.common.exception.RpcException;
import com.rpc.core.codec.CommonDecoder;
import com.rpc.core.codec.CommonEncoder;
import com.rpc.core.registery.ServiceRegistry;
import com.rpc.core.registery.impl.NacosServiceRegistry;
import com.rpc.core.remoting.handler.NettyClientHandler;
import com.rpc.core.message.RpcRequest;
import com.rpc.core.message.RpcResponse;
import com.rpc.core.serializer.impl.KryoSerializer;
import io.grpc.netty.shaded.io.grpc.netty.NettyChannelBuilder;
import io.grpc.netty.shaded.io.grpc.netty.NettyChannelProvider;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.AttributeKey;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author 21380
 */
@Slf4j
@NoArgsConstructor
public class NettyClient implements RpcClient {
    private static final Bootstrap bootstrap;
    private static final ServiceRegistry serviceRegistry = new NacosServiceRegistry();

    static {
        EventLoopGroup group = new NioEventLoopGroup();
        bootstrap = new Bootstrap();
        bootstrap.group(group)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.SO_KEEPALIVE, true)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) {
                        ChannelPipeline pipeline = ch.pipeline();
                        pipeline.addLast(new CommonDecoder())
                                .addLast(new CommonEncoder(new KryoSerializer()))
                                .addLast(new NettyClientHandler());
                    }
                });
    }

    @Override
    public Object sendRequest(RpcRequest rpcRequest) {
        try {
            log.info("request:"+rpcRequest);
            InetSocketAddress inetSocketAddress = serviceRegistry.lookupService(rpcRequest.getInterfaceName());
            Channel channel = bootstrap.connect(inetSocketAddress.getHostName(), inetSocketAddress.getPort()).sync().channel();
            log.info("客户端连接到服务器{}:{}", inetSocketAddress.getHostName(), inetSocketAddress.getPort());
            if(channel != null) {
                channel.writeAndFlush(rpcRequest).addListener(future1 -> {
                    if(future1.isSuccess()) {
                        log.info(String.format("客户端发送消息: %s", rpcRequest));
                    } else {
                        log.error("发送消息时有错误发生: ", future1.cause());
                    }
                });
                channel.closeFuture().sync();
                AttributeKey<RpcResponse> key = AttributeKey.valueOf("rpcResponse");
                RpcResponse rpcResponse = channel.attr(key).get();
                return rpcResponse.getData();
            }
        } catch (InterruptedException e) {
            log.error("发送消息时有错误发生: ", e);
        }
        return null;
    }
}