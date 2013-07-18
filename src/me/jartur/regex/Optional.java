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
class Optional extends Unary {

    Optional(AST child) {
        super(child);
    }

    @Override
    public List<Instruction> generateCode(int pc) {
        List<Instruction> instructions = new LinkedList<>();
        final List<Instruction> childCode = child.generateCode(pc + 1);
        instructions.add(new SplitInstruction(pc + 1, pc + 1 + childCode.size()));
        instructions.addAll(childCode);
        return instructions;
    }
}
