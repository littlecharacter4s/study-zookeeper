package com.lc.zk.common;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.util.Map;

public final class ProviderHelper {
    private ProviderHelper() {
    }

    public static void registry(String host, int port, Map<Class<?>, Remote> providers) {
        try {
            LocateRegistry.createRegistry(port);
            providers.forEach((key, value) -> {
                String url = String.format("rmi://%s:%d/%s", host, port, value.getClass().getName());
                try {
                    Naming.rebind(url, value);
                } catch (RemoteException | MalformedURLException e) {
                    e.printStackTrace();
                }
                String serviceName = key.getName();
                ZkHelper.getInstance().registry(serviceName, url);
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
