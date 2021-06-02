package com.zenglh.jdk.spi.impl;

import com.zenglh.jdk.spi.DataSourceSpi;

/**
 * @author zenglh
 * @date 2021/6/2 10:50
 */
public class MysqlDataSource implements DataSourceSpi {

    public void connection(String urlName) {
        System.out.println("MySQL Connection url:" + urlName);
    }
}
