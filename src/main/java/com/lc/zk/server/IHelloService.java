package com.lc.zk.server;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IHelloService extends Remote{
	String sayHello(String s) throws RemoteException;
}
