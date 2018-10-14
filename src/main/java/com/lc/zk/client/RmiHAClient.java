package com.lc.zk.client;

import com.lc.zk.server.IHelloService;

public class RmiHAClient {
	public static void main(String[] args) throws Exception {
		ServiceConsumer sc = new ServiceConsumer();
		while (true) {
			IHelloService ihs = sc.lookup();
			String result = ihs.sayHello("GJX");
			System.out.println(result);
			Thread.sleep(5000);
		}
	}
}
