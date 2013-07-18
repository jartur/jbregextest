package me.jartur.regex.vm.instructions;

import me.jartur.regex.vm.VMState;

/**
* Created by IntelliJ IDEA.
* User: jt
* Date: 7/18/13
* Time: 11:36 PM
*/
public class JumpInstruction implements Instruction {
    private final int pc;

    public JumpInstruction(int pc) {
        this.pc = pc;
    }

    @Override
    public boolean consumesChar() {
        return false;
    }

    @Override
    public void run(VMState state, int thread) {
        state.jump(thread, pc);
    }
}
