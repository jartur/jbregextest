package me.jartur.regex;

import me.jartur.regex.vm.instructions.Instruction;

import java.util.Collections;
import java.util.List;

/**
* Created by IntelliJ IDEA.
* User: jt
* Date: 7/18/13
* Time: 11:42 PM
*/
class EmptyNode implements AST {
    @Override
    public List<Instruction> generateCode(int pc) {
        return Collections.emptyList();
    }
}
