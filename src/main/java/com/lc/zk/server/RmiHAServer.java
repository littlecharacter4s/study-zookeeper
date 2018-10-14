package com.lc.zk.server;

public class RmiHAServer {
	public static void main(String[] args) throws Exception {
		String host = "127.0.0.1";
		int port = 1113;
		ServiceProvider sp = new ServiceProvider();
		IHelloService hs = new HelloService();
		sp.publish(hs, host, port);
		
		//Thread.sleep(Long.MAX_VALUE);
	}
}
