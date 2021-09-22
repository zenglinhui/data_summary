package com.zenglinhui.bytecode.base;

import java.lang.management.ManagementFactory;

/**
 * @author zenglh
 * @date 2021/9/22 18:31
 */
public class Base2 {

    public static void main(String[] args) {
        String name = ManagementFactory.getRuntimeMXBean().getName();
        String s = name.split("@")[0];
        System.out.println("pid:" + s);
        while (true) {
            try {
                Thread.sleep(5000L);
            } catch (InterruptedException e) {
                e.printStackTrace();
                break;
            }
            process();
        }
    }

    private static void process() {
        System.out.println("process");
    }

}
