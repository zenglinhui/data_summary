package com.zenglinhui.bytecode.javassist;

import com.zenglinhui.bytecode.base.Base;
import javassist.*;

import java.io.IOException;

/**
 * @author zenglh
 * @date 2021/9/22 16:50
 */
public class JavassistTest {

    public static void main(String[] args) throws NotFoundException, CannotCompileException, IOException, IllegalAccessException, InstantiationException {
        ClassPool cp = ClassPool.getDefault();
        CtClass cc = cp.get("com.zenglinhui.bytecode.base.Base");
        CtMethod m = cc.getDeclaredMethod("process");
        m.insertBefore("{ System.out.println(\"start\");}");
        m.insertAfter("System.out.println(\"end\");");
        Class c = cc.toClass();
        cc.writeFile();
        Base h = (Base) c.newInstance();
        h.process();

    }

}
