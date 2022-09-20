package com.lc.zk.common;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;

public final class ZkHelper {
    private static ZooKeeper zooKeeper;
    private static CountDownLatch latch = new CountDownLatch(1);

    private ZkHelper() {
        try {
            zooKeeper = new ZooKeeper(PropertyUtil.getInstance().getProperty(Constants.RMI_ZOOKEEPER), 5000, event -> {
                if (event.getState() == Watcher.Event.KeeperState.SyncConnected) {
                    System.out.println("zk连接成功!");
                    latch.countDown();
                } else {
                    System.err.println("zk连接失败!");
                }
            });
            latch.await();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static class ZkHelperInner {
        private static final ZkHelper zkHelper = new ZkHelper();

        private ZkHelperInner() {
        }
    }

    public static ZkHelper getInstance() {
        return ZkHelperInner.zkHelper;
    }

    public void registry(String serviceName, String url) {
        try {
            byte[] data = url.getBytes();
            String encodeUrl = URLEncoder.encode(url, "UTF-8");
            String rootPath = Constants.ZK_RMI;
            String clusterPath = rootPath + "/" + PropertyUtil.getInstance().getProperty(Constants.RMI_CLUSTER);
            String servicePath = clusterPath + "/" + serviceName;
            String path = servicePath + "/" + encodeUrl;
            if (null == zooKeeper.exists(rootPath, false)) {
                zooKeeper.create(rootPath, "".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
            }
            if (null == zooKeeper.exists(clusterPath, false)) {
                zooKeeper.create(clusterPath, "".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
            }
            if (null == zooKeeper.exists(servicePath, false)) {
                zooKeeper.create(servicePath, "".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
            }
            zooKeeper.create(path, data, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void discovery(String cluster, ConcurrentHashMap<String, List<String>> serverMap) {
        try {
            String path = Constants.ZK_RMI + "/" + cluster;
            List<String> nodeList = zooKeeper.getChildren(path, event -> {
                if (event.getType() == Watcher.Event.EventType.NodeChildrenChanged) {
                    discovery(cluster, serverMap);
                }
            });
            if (nodeList == null || nodeList.isEmpty()) {
                return;
            }
            for (String node : nodeList) {
                List<String> childNodes = zooKeeper.getChildren(path + "/" + node, event -> {
                    if (event.getType() == Watcher.Event.EventType.NodeChildrenChanged) {
                        discovery(cluster, serverMap);
                    }
                });
                if (childNodes == null || childNodes.isEmpty()) {
                    continue;
                }
                List<String> dataList = new ArrayList<>();
                for (String child : childNodes) {
                    byte[] data = zooKeeper.getData(path + "/" + node + "/" + child, false, null);
                    dataList.add(new String(data));
                }
                serverMap.put(cluster + ":" + node, dataList);
            }
            System.out.println(serverMap);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void test() {
        System.out.println(zooKeeper.getState().isAlive());
        System.out.println(zooKeeper.getState().isConnected());
    }
}
