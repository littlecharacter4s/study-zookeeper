package com.lc.zk.common;

import java.rmi.Naming;
import java.rmi.Remote;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

public class ConsumerHelper {
    private static ConcurrentHashMap<String, List<String>> serverMap = new ConcurrentHashMap();

    private ConsumerHelper() {
    }

    public static void discovery() {
        String[] clusters = new String[]{PropertyUtil.getInstance().getProperty(Constants.RMI_CLUSTER)};
        for (String cluster : clusters) {
            ZkHelper.getInstance().discovery(cluster, serverMap);
        }
    }

    public static <T extends Remote> T lookup(String clusterName, Class<?> clazz) {
        T service = null;
        List<String> urlList = serverMap.get(clusterName + ":" + clazz.getName());
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
                    service = lookup(clusterName, clazz);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                e.printStackTrace();
            }
        }
        return service;
    }
}
