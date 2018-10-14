package com.lc.zk.server;

import com.lc.zk.ZkConnector;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;

import java.rmi.Naming;
import java.rmi.Remote;
import java.rmi.registry.LocateRegistry;

public class ServiceProvider {
	public void publish(Remote remote, String host, int port) {
		String url = publishService(remote, host, port);
		if (url != null) {
			ZooKeeper zk = ZkConnector.connect();
			if (zk != null) {
				createNode(zk, url);
			}
		}
	}

	private String publishService(Remote remote, String host, int port) {
		String url = null;
		url = String.format("rmi://%s:%d/%s", host, port,remote.getClass().getName());
		try {
			LocateRegistry.createRegistry(port);
			Naming.rebind(url, remote);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return url;
	}
	
	private void createNode(ZooKeeper zk, String url) {
		byte[] data = url.getBytes();
		try {
			zk.create(ZkConnector.ZK_PROVIDER_PATH, data, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
