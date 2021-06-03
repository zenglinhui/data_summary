package com.zenglh.custom.annotation;

import java.lang.annotation.*;

/**
 * @author zenglh
 * @date 2021/6/2 19:43
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface SPI {

    String value() default "";

}
