package com.zenglinhui.bytecode.asm.core;

import jdk.internal.org.objectweb.asm.ClassReader;
import jdk.internal.org.objectweb.asm.ClassVisitor;
import jdk.internal.org.objectweb.asm.ClassWriter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * @author zenglh
 * @date 2021/9/22 15:24
 */
public class Generator {

    public static void main(String[] args) throws IOException {
        // 读取
        ClassReader classReader = new ClassReader("com/zenglinhui/bytecode/base/Base");
        ClassWriter classWriter = new ClassWriter(ClassWriter.COMPUTE_MAXS);

        // 处理
        ClassVisitor classVisitor = new MyClassVisitor(classWriter);
        classReader.accept(classVisitor, ClassReader.SKIP_DEBUG);
        byte[] data = classWriter.toByteArray();

        // 输出
        File f = new File("F:/data/java训练营/学习汇总/data_summary/note/java/bytecode/demo/target/classes/com/zenglinhui/bytecode/base/Base.class");
        FileOutputStream out = new FileOutputStream(f);
        out.write(data);
        out.close();
        System.out.println("now generator cc success!!");

    }

}
