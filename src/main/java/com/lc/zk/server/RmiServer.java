package com.lc.zk.server;

import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;

public class RmiServer {
	public static void main(String[] args) throws Exception {
		int port = 1111;
		String uri = "rmi://127.0.0.1:1111/com.lc.zk.server.HelloService";
		LocateRegistry.createRegistry(port);
		Naming.rebind(uri, new HelloService());
	}
}
