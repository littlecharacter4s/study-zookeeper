package com.lc.zk.provider;

import com.lc.zk.common.ProviderHelper;
import com.lc.zk.contract.HelloService;
import com.lc.zk.provider.component.HelloServiceImpl;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class Main {
    public static void main(String[] args) throws UnknownHostException, RemoteException, InterruptedException {
        String ip = InetAddress.getLocalHost().getHostAddress();
        int port = 3073;
        Map<Class<?>, Remote> providers = new HashMap<>();
        HelloService helloService = new HelloServiceImpl();
        providers.put(HelloService.class, helloService);
//        UserService userService = new UserServiceImpl();
//        providers.put(UserService.class, userService);
        ProviderHelper.registry(ip, port, providers);
        TimeUnit.SECONDS.sleep(3600);
    }
}
