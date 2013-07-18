package me.jartur.regex;

import me.jartur.regex.vm.instructions.Instruction;
import me.jartur.regex.vm.instructions.JumpInstruction;
import me.jartur.regex.vm.instructions.MatchInstruction;
import me.jartur.regex.vm.instructions.SplitInstruction;

import java.util.LinkedList;
import java.util.List;

/**
* Created by IntelliJ IDEA.
* User: jt
* Date: 7/18/13
* Time: 11:42 PM
*/
class Alternate implements AST {
    private final AST left;
    private final AST right;

    public Alternate(AST left, AST right) {
        this.left = left;
        this.right = right;
    }

    @Override
    public List<Instruction> generateCode(int pc) {
        List<Instruction> instructions = new LinkedList<>();
        List<Instruction> leftInstructions = left.generateCode(pc + 1);
        leftInstructions.add(new MatchInstruction());
        final int rightOffset = pc + leftInstructions.size();
        instructions.add(new SplitInstruction(pc + 1, rightOffset + 1));
        final List<Instruction> rightInstructions = right.generateCode(rightOffset);
        leftInstructions.set(leftInstructions.size() - 1, new JumpInstruction(pc + leftInstructions.size() + rightInstructions.size() + 1));
        instructions.addAll(leftInstructions);
        instructions.addAll(rightInstructions);
        return instructions;
    }
}
