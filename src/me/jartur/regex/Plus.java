package me.jartur.regex;

import me.jartur.regex.vm.instructions.Instruction;
import me.jartur.regex.vm.instructions.SplitInstruction;

import java.util.LinkedList;
import java.util.List;

/**
* Created by IntelliJ IDEA.
* User: jt
* Date: 7/18/13
* Time: 11:43 PM
*/
class Plus extends Unary {

    Plus(AST child) {
        super(child);
    }

    @Override
    public List<Instruction> generateCode(int pc) {
        List<Instruction> instructions = new LinkedList<>();
        final List<Instruction> childCode = child.generateCode(pc);
        instructions.addAll(childCode);
        instructions.add(new SplitInstruction(pc, pc + 1 + childCode.size()));
        return instructions;
    }
}
