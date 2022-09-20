package com.lc.zk.consumer;

import com.lc.zk.common.ConsumerHelper;
import com.lc.zk.contract.HelloService;
import com.lc.zk.contract.UserService;

import java.rmi.RemoteException;
import java.util.concurrent.TimeUnit;

public class Main {
    public static void main(String[] args) throws RemoteException, InterruptedException {
        ConsumerHelper.discovery();
        System.out.println("go...");
        while (true) {
            HelloService helloService = ConsumerHelper.lookup("rmi_study", HelloService.class);
            System.out.println(helloService.sayHello("World"));
            // UserService userService = ConsumerHelper.lookup("rmi_study", UserService.class);
            // System.out.println(userService.getUser());
            TimeUnit.SECONDS.sleep(1);
        }
    }
}
