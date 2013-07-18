package me.jartur.regex;

/**
* Created by IntelliJ IDEA.
* User: jt
* Date: 7/18/13
* Time: 11:43 PM
*/
abstract class Unary implements AST {
    protected final AST child;

    Unary(AST child) {
        this.child = child;
    }
}
