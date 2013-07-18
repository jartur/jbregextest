package me.jartur.regex.vm.instructions;

import me.jartur.regex.vm.VMState;

/**
* Created by IntelliJ IDEA.
* User: jt
* Date: 7/18/13
* Time: 11:36 PM
*/
public class SplitInstruction implements Instruction {
    private final int x, y;

    public SplitInstruction(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public boolean consumesChar() {
        return false;
    }

    @Override
    public void run(VMState state, int thread) {
        state.split(thread, x, y);
    }
}
