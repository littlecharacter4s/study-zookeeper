package com.lc.zk;

import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

import java.util.concurrent.CountDownLatch;

public class ZkConnector {
    public static final String ZK_CONNECTION_STRING = "zk1:2181,zk2:2182,zk3:2183";
    public static final int ZK_SESSION_TIMEOUT = 5000;
    public static final String ZK_REGISTRY_PATH = "/registry";
    public static final String ZK_PROVIDER_PATH = ZK_REGISTRY_PATH + "/provider";

    private static CountDownLatch latch = new CountDownLatch(1);

    private ZkConnector() {}

    public static ZooKeeper connect() {
        ZooKeeper zk = null;
        try {
            zk = new ZooKeeper(ZK_CONNECTION_STRING, ZK_SESSION_TIMEOUT, event -> {
                if (event.getState() == Watcher.Event.KeeperState.SyncConnected) {
                    latch.countDown();
                }
            });
            latch.await();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return zk;
    }
}
