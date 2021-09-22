package com.zenglinhui.jvm;

/**
 * @author zenglh
 * @date 2021/3/11 18:05
 */
public class validateVolatile1 {

    private static volatile int count = 1;

    public static void main(String[] args) {
        System.out.println(count);
    }

}
