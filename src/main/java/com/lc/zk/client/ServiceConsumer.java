package com.lc.zk.client;

import com.lc.zk.ZkConnector;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

import java.rmi.Naming;
import java.rmi.Remote;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;


public class ServiceConsumer {
	private volatile List<String> urlList = new ArrayList<String>();
	
	public ServiceConsumer() {
		ZooKeeper zk = ZkConnector.connect();
		if (zk != null) {
			watchNode(zk);
		}
	}
	
	private void watchNode(final ZooKeeper zk) {
		try {
			List<String> nodeList = zk.getChildren(ZkConnector.ZK_REGISTRY_PATH, event -> {
                if (event.getType() == Watcher.Event.EventType.NodeChildrenChanged) {
                    watchNode(zk);
                }
            });
			
			List<String> dataList = new ArrayList<>();
			for (String node : nodeList) {
				byte[] data = zk.getData(ZkConnector.ZK_REGISTRY_PATH + "/" + node, false, null);
				dataList.add(new String(data));
			}
			
			urlList = dataList;
		} catch (Exception e) {
			e.printStackTrace();
		}		
	}

	public <T extends Remote> T lookup() {
		T service = null;
		int size = urlList.size();
		if (size > 0) {
			String url = urlList.get(ThreadLocalRandom.current().nextInt(size));
			System.out.println(url);
			
			try {
				service = (T) Naming.lookup(url);
			} catch (Exception e) {
				try {
					//zookeeper更新节点需要投票时间，所以此步骤有可能报错
					TimeUnit.MICROSECONDS.sleep(3000);
					service = lookup();
				} catch (Exception ex) {
					ex.printStackTrace();
				} 
				e.printStackTrace();
			}
		}
		return service;
	}
}
