package com.lc.zk.provider.component;

import com.lc.zk.contract.HelloService;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class HelloServiceImpl extends UnicastRemoteObject implements HelloService {
    public HelloServiceImpl() throws RemoteException {}

    @Override
    public String sayHello(String name) throws RemoteException {
        return "Hello " + name;
    }
}
