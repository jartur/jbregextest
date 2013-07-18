package me.jartur.regex.vm;

import me.jartur.regex.vm.instructions.Instruction;

import java.util.Collections;
import java.util.List;

/**
* Created by IntelliJ IDEA.
* User: jt
* Date: 7/18/13
* Time: 11:32 PM
*/
public class VMRunner {
    private final List<Instruction> program;

    public VMRunner(List<Instruction> program) {
        this.program = Collections.unmodifiableList(program);
    }

    public boolean match(String str) {
        final VMState state = new VMState(str);
        while (state.incrementSp()) {
            Integer currentThread = state.getNextThread();
            while(currentThread != null) {
                boolean finished = false;
                while (!finished){
                    final Integer pc = state.getPc(currentThread);
                    if(pc == null) {
                        throw new IllegalStateException("Current VM thread doesn't have a PC");
                    }

                    if(pc >= program.size()) {
                        return true;
                    }

                    final Instruction instruction = program.get(pc);
                    instruction.run(state, currentThread);
                    finished = instruction.consumesChar();
                }

                currentThread = state.getNextThread();
            }
        }

        return false;
    }

}
