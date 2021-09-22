package com.zenglinhui.bytecode.instrument;

import com.zenglinhui.bytecode.base.Base2;

import java.lang.instrument.Instrumentation;

/**
 * @author zenglh
 * @date 2021/9/22 18:25
 */
public class TestAgent {

    public static void agentmain(String args, Instrumentation instrumentation) {
        // 指定自定义的 transformer, 利用 javassist 做字节码替换
        instrumentation.addTransformer(new TestTransformer(), true);
        try {
            // 重定义类并载入新的字节码
            instrumentation.retransformClasses(Base2.class);
            System.out.println("agent load Done.");
        } catch (Exception e) {
            System.out.println("agent load failed!");
        }

    }

}
