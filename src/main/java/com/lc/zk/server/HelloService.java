package com.lc.zk.server;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class HelloService extends UnicastRemoteObject implements IHelloService{
	private static final long serialVersionUID = 1L;

	protected HelloService() throws RemoteException {}

	@Override
	public String sayHello(String s) throws RemoteException{
		return "Hello " + s;
	}
}
