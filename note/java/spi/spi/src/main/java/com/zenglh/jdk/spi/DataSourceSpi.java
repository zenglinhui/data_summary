package com.zenglh.jdk.spi;

/**
 * @author zenglh
 * @date 2021/6/2 10:50
 */
public interface DataSourceSpi {

    /**
     * 连接方法
     * @param urlName
     */
    void connection(String urlName);

}
