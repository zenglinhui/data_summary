package com.zenglinhui.java8.stream;

/**
 * 交易员
 * @author zenglh
 * @date 2021/10/13 14:57
 */
public class Trader {

    private final String name;

    private final String city;

    public Trader(String name, String city) {
        this.name = name;
        this.city = city;
    }

    public String getName() {
        return name;
    }

    public String getCity() {
        return city;
    }

    @Override
    public String toString() {
        return "Trader: " + this.name + " in " + this.city;
    }
}
