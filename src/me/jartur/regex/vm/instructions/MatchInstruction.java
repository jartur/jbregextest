package me.jartur.regex.vm.instructions;

import me.jartur.regex.vm.VMState;

/**
* Created by IntelliJ IDEA.
* User: jt
* Date: 7/18/13
* Time: 11:37 PM
*/
public class MatchInstruction implements Instruction {
    @Override
    public boolean consumesChar() {
        return false;
    }

    @Override
    public void run(VMState state, int thread) {
        state.incrementPc(thread);
    }
}
