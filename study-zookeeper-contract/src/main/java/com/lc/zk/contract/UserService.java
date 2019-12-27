package com.lc.zk.contract;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface UserService extends Remote {
    String getUser() throws RemoteException;
}

