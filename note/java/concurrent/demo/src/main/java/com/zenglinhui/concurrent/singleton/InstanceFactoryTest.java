package com.zenglinhui.concurrent.singleton;

/**
 * @author zenglh
 * @date 2021/9/30 14:56
 */
public class InstanceFactoryTest {

    public static void main(String[] args) {

        Instance instance1 = InstanceFactory.getInstance();
        Instance instance2 = InstanceFactory.getInstance();

        System.out.println(instance1 == instance2);
        System.out.println(instance1.equals(instance2));

    }

}
