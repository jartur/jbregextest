package me.jartur.regex.vm.instructions;

import me.jartur.regex.vm.VMState;
import me.jartur.regex.chargroups.CharGroup;

/**
* Created by IntelliJ IDEA.
* User: jt
* Date: 7/18/13
* Time: 11:35 PM
*/
public class CharSetInstruction implements Instruction {
    private final CharGroup c;

    public CharSetInstruction(CharGroup c) {
        this.c = c;
    }

    @Override
    public boolean consumesChar() {
        return true;
    }

    @Override
    public void run(VMState state, int thread) {
        if(c.in(state.getCurrentCodePoint()))
            state.incrementPc(thread);
        else state.stop(thread);
    }
}
