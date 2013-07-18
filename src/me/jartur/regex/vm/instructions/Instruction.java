package me.jartur.regex.vm.instructions;

import me.jartur.regex.vm.VMState;

/**
* Created by IntelliJ IDEA.
* User: jt
* Date: 7/18/13
* Time: 11:35 PM
*/
public interface Instruction {
    boolean consumesChar();
    void run(VMState state, int thread);
}
