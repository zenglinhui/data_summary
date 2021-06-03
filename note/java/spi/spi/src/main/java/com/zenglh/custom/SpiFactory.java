package com.zenglh.custom;

import com.zenglh.custom.annotation.SPI;

/**
 * @author zenglh
 * @date 2021/6/2 19:35
 */
@SPI
public interface SpiFactory {

    /**
     * get spi implements
     * @param key
     * @param klass
     * @param <A>
     * @return
     */
    <A> A getSpiImpl(String key, Class<A> klass);

}
