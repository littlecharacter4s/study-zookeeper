package com.lc.zk.provider.component;

import com.lc.zk.contract.UserService;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class UserServiceImpl extends UnicastRemoteObject implements UserService {
    public UserServiceImpl() throws RemoteException {}


    @Override
    public String getUser() throws RemoteException {
        return "{name=zhangsan,age=18,sex=1}";
    }
}
