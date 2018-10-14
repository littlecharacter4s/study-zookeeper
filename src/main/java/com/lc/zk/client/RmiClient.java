package com.lc.zk.client;

import com.lc.zk.server.IHelloService;

import java.rmi.Naming;

public class RmiClient {
	public static void main(String[] args) throws Exception {
		String uri = "rmi://127.0.0.1:1111/com.lc.zk.server.HelloService";
		IHelloService hs = (IHelloService) Naming.lookup(uri);
		String result = hs.sayHello("GJX");
		System.out.println(result);
	}
}
