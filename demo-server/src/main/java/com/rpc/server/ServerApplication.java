package com.rpc.server;

import com.rpc.core.annotation.RPCServiceScan;
import com.rpc.core.remoting.server.NettyServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author 21380
 */
@RPCServiceScan
public class ServerApplication {
    public static void main(String[] args) {
        // SpringApplication.run(ServerApplication.class, args);
        NettyServer server = new NettyServer("127.0.0.1", 9999);
        server.start();
    }
}
