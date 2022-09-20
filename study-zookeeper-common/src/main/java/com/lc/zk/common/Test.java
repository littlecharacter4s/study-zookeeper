package com.lc.zk.common;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

/**
 * @author gujixian
 * @since 2022/9/20
 */
public class Test {
    public static void main(String[] args) throws IOException, InterruptedException {
        final CountDownLatch countDownLatch = new CountDownLatch(1);
        ZooKeeper zooKeeper = new ZooKeeper("zk:2181,zk:2182,zk:2183", 3000, event -> {
            if (Watcher.Event.KeeperState.SyncConnected == event.getState()) {
                //如果收到了服务端的响应事件，连接成功
                System.out.println("连接成功！");
            } else {
                System.out.println("连接失败！");
            }
            countDownLatch.countDown();
        });
        countDownLatch.await();
        System.out.println(zooKeeper.getState());
    }
}
