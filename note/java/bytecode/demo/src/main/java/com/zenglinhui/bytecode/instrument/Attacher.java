package com.zenglinhui.bytecode.instrument;


import com.sun.tools.attach.AgentInitializationException;
import com.sun.tools.attach.AgentLoadException;
import com.sun.tools.attach.AttachNotSupportedException;
import com.sun.tools.attach.VirtualMachine;

import java.io.IOException;

/**
 * @author zenglh
 * @date 2021/9/23 14:11
 */
public class Attacher {

    public static void main(String[] args) throws IOException, AttachNotSupportedException, AgentLoadException, AgentInitializationException {
        VirtualMachine vm = VirtualMachine.attach("29420");
        vm.loadAgent("F:/data/java训练营/学习汇总/data_summary/note/java/bytecode/demo/src/main/java/com/zenglinhui/bytecode/testAgent.jar");
    }

}
