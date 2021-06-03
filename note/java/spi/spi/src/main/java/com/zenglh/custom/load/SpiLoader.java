package com.zenglh.custom.load;

import com.zenglh.custom.SpiFactory;

import java.util.Map;
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


    private SpiLoader(final Class<A> clazz) {
        this.clazz = clazz;
        if (clazz != SpiFactory.class) {

        }
    }




}
