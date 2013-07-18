package me.jartur.regex;

import me.jartur.regex.vm.instructions.Instruction;

import java.util.LinkedList;
import java.util.List;

/**
* Created by IntelliJ IDEA.
* User: jt
* Date: 7/18/13
* Time: 11:42 PM
*/
class Concatenate extends AST {
    private final AST left;
    private final AST right;

    Concatenate(AST left, AST right) {
        this.left = left;
        this.right = right;
    }

    @Override
    public List<Instruction> generateCode(int pc) {
        List<Instruction> instructions = new LinkedList<>();
        final List<Instruction> leftInstructions = left.generateCode(pc);
        final List<Instruction> rightInstructions = right.generateCode(pc + leftInstructions.size());
        instructions.addAll(leftInstructions);
        instructions.addAll(rightInstructions);
        return instructions;
    }
}
