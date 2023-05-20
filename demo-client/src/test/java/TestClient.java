import com.rpc.common.exception.RpcException;
import com.rpc.core.remoting.client.NettyClient;
import com.rpc.core.remoting.client.RpcClient;
import com.rpc.core.proxy.RpcClientProxy;
import com.rpc.server.service.HelloService;

public class TestClient {

    public static void main(String[] args) throws RpcException {
        // // 获取代理
        // RpcClientProxy proxy = new RpcClientProxy("127.0.0.1", 8081);
        // // 获取到服务实例
        // HelloService helloService = proxy.getProxy(HelloService.class);
        // System.out.println(helloService.hello("ni hao"));
        // HelloService helloService = new HelloServiceImpl();
        // ServiceProvider registry = new DefaultServiceProvider();
        // System.out.println("根据接口name拿到实现类:"+registry.getService(helloService.getClass().getInterfaces()[0].getCanonicalName()));

        // RpcClient client = new NettyClient("127.0.0.1", 8081, serviceRegistry);
        // RpcClientProxy rpcClientProxy = new RpcClientProxy(client);
        // HelloService helloService = rpcClientProxy.getProxy(HelloService.class);
        // System.out.println(helloService.hello("ni hao"));

        RpcClient client = new NettyClient();
        RpcClientProxy rpcClientProxy = new RpcClientProxy(client);
        HelloService helloService = rpcClientProxy.getProxy(HelloService.class);
        System.out.println(helloService.hello("hhhhhhh"));
    }
}
