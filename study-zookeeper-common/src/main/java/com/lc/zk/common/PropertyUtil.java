package com.lc.zk.common;

import org.apache.commons.lang3.StringUtils;

import java.net.URL;
import java.util.Optional;
import java.util.Properties;

public final class PropertyUtil {
    private Properties properties;

    private PropertyUtil() {
        properties = new Properties();
        try {
            // 默认配置文件放在classpath下，可以通过JVM参数：-Drmi.properties.file=conf/rmi.properties来指定位置
            String rmiPath = System.getProperty("rmi.properties.file");
            if (StringUtils.isEmpty(rmiPath)) {
                rmiPath = "rmi.properties";
            }
            URL url = PropertyUtil.class.getClassLoader().getResource(rmiPath);
            assert url != null;
            properties.load(url.openStream());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static class PropertyUtilInner {
        private static final PropertyUtil propertiesUtil = new PropertyUtil();

        private PropertyUtilInner() {
        }
    }

    public static PropertyUtil getInstance() {
        return PropertyUtilInner.propertiesUtil;
    }

    public String getProperty(String key) {
        if (StringUtils.isEmpty(key)) {
            return "";
        }
        return Optional.ofNullable(properties.getProperty(key)).orElse("");
    }
}
