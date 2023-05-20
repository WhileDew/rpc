import com.rpc.common.exception.RpcException;
import com.rpc.core.remoting.server.NettyServer;

public class TestServer {
    public static void main(String[] args) throws RpcException {
        // // 注册服务
        // HelloService helloService = new HelloServiceImpl();
        // RpcServer rpcServer = new RpcServer();
        // // 注册服务,127.0.0.1:8081
        // rpcServer.register(helloService, 8081);


        // HelloService helloService = new HelloServiceImpl();
        // // 创建服务注册的实现类注册服务，RpcServer注入服务，然后启动服务
        // ServiceProvider serviceRegistry = new DefaultServiceProvider();
        // serviceRegistry.register(helloService);
        // RpcServer rpcServer = new RpcServer(serviceRegistry);
        // // 使用rpcserver开启服务
        // rpcServer.start(8081);

        // HelloService helloService = new HelloServiceImpl();
        // ServiceProvider registry = new DefaultServiceProvider();
        // registry.register(helloService);
        // NettyServer server = new NettyServer();
        // server.start(8081);

        NettyServer server = new NettyServer("127.0.0.1", 9999);
        server.start();
    }
}
