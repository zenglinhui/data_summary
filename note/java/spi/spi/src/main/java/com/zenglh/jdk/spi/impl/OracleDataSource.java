package com.zenglh.jdk.spi.impl;

import com.zenglh.jdk.spi.DataSourceSpi;

/**
 * @author zenglh
 * @date 2021/6/2 10:51
 */
public class OracleDataSource implements DataSourceSpi {

    @Override
    public void connection(String urlName) {
        System.out.println("Oracle Connection url:" + urlName);
    }
}
