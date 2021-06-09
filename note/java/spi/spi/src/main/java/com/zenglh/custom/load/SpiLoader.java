package com.zenglh.custom.load;

import com.zenglh.custom.SpiFactory;
import com.zenglh.custom.annotation.SPI;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author zenglh
 * @date 2021/6/2 19:40
 */
public final class SpiLoader<A> {

    /**
     * 指定配置文件位置
     */
    private static final String ZLH_DIRECTORY = "META-INF/zlh/";

    private static final Map<Class<?>, SpiLoader<?>> LOADER_MAP = new ConcurrentHashMap<>();

    private final Class<A> clazz;

    private final Holder<Map<String, Class<?>>> cachedClasses = new Holder<>();

    private final Map<String, Holder<Object>> cachedInstances = new ConcurrentHashMap<>();

    private final Map<Class<?>, Object> joinInstances = new ConcurrentHashMap<>();

    private String cachedDefaultName;


    private SpiLoader(final Class<A> clazz) {
        this.clazz = clazz;
        if (clazz != SpiFactory.class) {
            SpiLoader.getSpiLoader(SpiFactory.class).getSpiClasses();
        }
    }

    public Map<String, Class<?>> getSpiClasses() {
        Map<String, Class<?>> classMap = cachedClasses.getValue();
        if (null == classMap) {
            synchronized(cachedClasses) {
                classMap = cachedClasses.getValue();
                if (null == classMap) {
                    classMap = loadSpiClass();
                    cachedClasses.setValue(classMap);
                }
            }
        }
        return classMap;
    }

    private Map<String, Class<?>> loadSpiClass() {
        SPI annotation = clazz.getAnnotation(SPI.class);
        if (null != annotation) {
            String value = annotation.value();
            if (StringUtils.isNotBlank(value)) {
                cachedDefaultName = value;
            }
        }
        Map<String, Class<?>> classMap = new HashMap<>(4);
        loadDirectory(classMap);
        return classMap;
    }

    private void loadDirectory(final Map<String, Class<?>> classMap) {
        String fileName = ZLH_DIRECTORY + clazz.getName();
        ClassLoader classLoader = SpiLoader.class.getClassLoader();
        try {
            Enumeration<URL> urlEnumeration = classLoader != null ? classLoader.getResources(fileName)
                    : ClassLoader.getSystemResources(fileName);
            if (null != urlEnumeration) {
                while (urlEnumeration.hasMoreElements()) {
                    URL url = urlEnumeration.nextElement();
                    loadResources(classMap, url);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadResources(final Map<String, Class<?>> classMap, final URL url) {

        try (InputStream inputStream = url.openStream()) {
            Properties properties = new Properties();
            properties.load(inputStream);
            properties.forEach((k, v) -> {
                String name = (String) k;
                String classPath = (String) v;
                if (StringUtils.isNotBlank(name) && StringUtils.isNotBlank(classPath)) {
                    try {
                        loadClass(classMap, name, classPath);
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void loadClass(final Map<String, Class<?>> classMap, final String name, final String classPath)
            throws ClassNotFoundException {

        Class<?> subClazz = Class.forName(classPath);
        if (!clazz.isAssignableFrom(subClazz)) {
            throw new IllegalStateException("load spi resources error, " + subClazz + "subtype is not of " + clazz);
        }
        Class<?> oldClass = classMap.get(name);
        if (null == oldClass) {
            classMap.put(name, subClazz);
        } else {
            throw new IllegalStateException("load spi resources error, Duplicate class " + clazz.getName() + "name"
                    + name + " on " + oldClass.getName() + " or " + subClazz.getName());
        }

    }

    public static <A> SpiLoader<A> getSpiLoader(final Class<A> clazz) {
        if (null == clazz) {
            throw new NullPointerException("spi class is null");
        }
        if (!clazz.isInterface()) {
            throw new IllegalArgumentException("spi clazz (" + clazz + ") is not interface!");
        }
        if (!clazz.isAnnotationPresent(SPI.class)) {
            throw new IllegalArgumentException("spi clazz (" + clazz + ") without @" + SPI.class + "Annotation");
        }
        SpiLoader<A> spiLoader = (SpiLoader<A>)LOADER_MAP.get(clazz);
        if (null != spiLoader) {
            return spiLoader;
        }
        LOADER_MAP.putIfAbsent(clazz, new SpiLoader<>(clazz));
        return (SpiLoader<A>)LOADER_MAP.get(clazz);
    }

    public A getDefaultJoin() {
        getSpiClasses();
        if (StringUtils.isBlank(cachedDefaultName)) {
            return null;
        }
        return getJoin(cachedDefaultName);
    }

    private A getJoin(final String name) {

        if (StringUtils.isBlank(name)) {
            throw new NullPointerException("get join name is null");
        }
        Holder<Object> objectHolder = cachedInstances.get(name);
        if (null == objectHolder) {
            cachedInstances.putIfAbsent(name, new Holder<>());
            objectHolder = cachedInstances.get(name);
        }
        Object value = objectHolder.getValue();
        if (null == value) {
            synchronized (cachedInstances) {
                value = objectHolder.getValue();
                if (null == value) {
                    value = createSpi(name);
                    objectHolder.setValue(value);
                }
            }
        }
        return (A) value;
    }

    private A createSpi(final String name) {

        Class<?> clazz = getSpiClasses().get(name);
        if (null == clazz) {
            throw new IllegalArgumentException("name is error");
        }
        Object obj = joinInstances.get(clazz);
        if (null == obj) {
            try {
                joinInstances.putIfAbsent(clazz, clazz.newInstance());
                obj = joinInstances.get(clazz);
            } catch (InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return (A) obj;
    }


    public static class Holder<T> {

        private volatile T value;

        /**
         * Gets value.
         *
         * @return the value
         */
        public T getValue() {
            return value;
        }

        /**
         * Sets value.
         *
         * @param value the value
         */
        public void setValue(final T value) {
            this.value = value;
        }
    }


}
