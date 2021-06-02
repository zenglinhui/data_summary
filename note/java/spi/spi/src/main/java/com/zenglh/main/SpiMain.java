package com.zenglh.main;

import com.zenglh.spi.DataSourceSpi;

import java.util.ServiceLoader;

/**
 * @author zenglh
 * @date 2021/6/2 11:06
 */
public class SpiMain {

    public static void main(String[] args) {
        ServiceLoader<DataSourceSpi> dataSourceSpis = ServiceLoader.load(DataSourceSpi.class);
        for (DataSourceSpi spi :
                dataSourceSpis) {
            spi.connection("10.200.89.23:3306");
        }

    }

}
