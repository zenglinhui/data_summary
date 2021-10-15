package com.zenglinhui.concurrent.singleton;

/**
 * 基于类初始化的单例
 * @author zenglh
 * @date 2021/9/30 14:53
 */
public class InstanceFactory {

    private static class InstanceHolder {
        static Instance instance = new Instance();
    }

    public static Instance getInstance() {
        return InstanceHolder.instance;
    }

}
