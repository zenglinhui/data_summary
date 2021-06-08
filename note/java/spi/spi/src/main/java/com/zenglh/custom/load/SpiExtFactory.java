package com.zenglh.custom.load;

import com.zenglh.custom.SpiFactory;
import com.zenglh.custom.annotation.SPI;

/**
 * @author zenglh
 * @date 2021/6/7 18:51
 */
public class SpiExtFactory implements SpiFactory {
    @Override
    public <A> A getSpiImpl(final String key, final Class<A> klass) {
        if (klass.isInterface() && klass.isAnnotationPresent(SPI.class)) {
            SpiLoader<A> spiLoader = SpiLoader.getSpiLoader(klass);
            return null;
        }
        return null;
    }
}
