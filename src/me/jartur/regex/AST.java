package me.jartur.regex;

import me.jartur.regex.vm.instructions.Instruction;

import java.util.List;

/**
* Created by IntelliJ IDEA.
* User: jt
* Date: 7/18/13
* Time: 11:43 PM
*/
interface AST {
    List<Instruction> generateCode(int pc);
}
