package com.rpc.core.remoting.handler;

import com.alibaba.nacos.api.naming.pojo.Instance;
import com.rpc.common.exception.RpcException;
import com.rpc.core.discovery.ServiceDiscovery;
import com.rpc.core.discovery.impl.NacosServiceDiscovery;
import com.rpc.core.message.RpcRequest;
import com.rpc.core.message.RpcResponse;
import com.rpc.core.provider.ServiceProvider;
import com.rpc.core.provider.impl.DefaultServiceProvider;
import com.rpc.core.utils.NacosUtil;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.ReferenceCountUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * @author 21380
 */
@Slf4j
public class NettyServerHandler extends SimpleChannelInboundHandler<RpcRequest> {

    private static final RequestHandler REQUEST_HANDLER;
    private static final ServiceProvider SERVICE_PROVIDER;

    static {
        REQUEST_HANDLER = new RequestHandler();
        SERVICE_PROVIDER = new DefaultServiceProvider();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RpcRequest msg) {
        try {
            log.info("服务器接收到请求: {}", msg);
            // 获取信息
            String interfaceName = msg.getInterfaceName();
            // 获取实现类
            Object service = SERVICE_PROVIDER.getService(interfaceName);
            Object result = REQUEST_HANDLER.handle(msg, service);
            RpcResponse response = RpcResponse.success(result);
            log.info("服务端发送响应：" + response);
            ChannelFuture future = ctx.writeAndFlush(response);
            // 关闭监听器
            future.addListener(ChannelFutureListener.CLOSE);
        } catch (RpcException e) {
            throw new RuntimeException(e);
        } finally {
            ReferenceCountUtil.release(msg);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.error("处理过程调用时有错误发生:");
        cause.printStackTrace();
        ctx.close();
    }
}
