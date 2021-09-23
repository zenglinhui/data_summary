package com.zenglinhui.bytecode.bytebuddy;

import net.bytebuddy.ByteBuddy;
import net.bytebuddy.description.method.MethodDescription;
import net.bytebuddy.implementation.FixedValue;
import net.bytebuddy.matcher.ElementMatchers;

/**
 * @author zenglh
 * @date 2021/9/23 15:24
 */
public class TestByteBuddy {

    public static void main(String[] args) throws IllegalAccessException, InstantiationException {
        Class<?> dynamicType = new ByteBuddy()
                .subclass(Object.class)
                .method(ElementMatchers.<MethodDescription>named("toString"))
                .intercept(FixedValue.value("Hello World!"))
                .make()
                .load(TestByteBuddy.class.getClassLoader())
                .getLoaded();
        System.out.println(dynamicType.newInstance().toString());
    }

}
