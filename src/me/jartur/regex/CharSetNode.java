package me.jartur.regex;

import me.jartur.regex.chargroups.CharGroup;
import me.jartur.regex.vm.instructions.CharSetInstruction;
import me.jartur.regex.vm.instructions.Instruction;

import java.util.LinkedList;
import java.util.List;

/**
* Created by IntelliJ IDEA.
* User: jt
* Date: 7/18/13
* Time: 11:42 PM
*/
class CharSetNode implements AST {
    private final CharGroup set;

    CharSetNode(CharGroup set) {
        this.set = set;
    }

    @Override
    public List<Instruction> generateCode(int pc) {
        List<Instruction> instructions = new LinkedList<>();
        instructions.add(new CharSetInstruction(set));
        return instructions;
    }
}
