package com.zenglh.spi.impl;

import com.zenglh.spi.DataSourceSpi;

/**
 * @author zenglh
 * @date 2021/6/2 10:51
 */
public class OracleDataSource implements DataSourceSpi {

    public void connection(String urlName) {
        System.out.println("Oracle Connection url:" + urlName);
    }
}
