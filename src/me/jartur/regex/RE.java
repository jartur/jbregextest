package me.jartur.regex;

import me.jartur.regex.chargroups.*;
import me.jartur.regex.vm.*;
import me.jartur.regex.vm.instructions.*;

import java.util.*;

/**
* Created by IntelliJ IDEA.
* User: jt
* Date: 7/18/13
* Time: 11:32 PM
*/
public class RE {
    private final VMRunner vm;

    RE(VMRunner vm) {
        this.vm = vm;
    }

    /**
     * This is the method to compile a regular expression for later matching.
     * Supported stuff is characters, [groups], ?, *, +, |, [^complement groups], \d, \w and .
     * Match always begins at the beginning of a string, ^ as a marker is implied. If you need
     * different behaviour please put .* at the beginning.
     * $ as an end-of-string match character is not supported.
     * @param regex A regular expression
     * @return new instance of a compiled matcher
     */
    public static RE compile(String regex) {
        final OffsetAndAST offsetAndAST = compile1(regex, 0, 0);
        List<Instruction> instructions = offsetAndAST.getTree().generateCode(0);
        instructions.add(new MatchInstruction());
        return new RE(new VMRunner(instructions));
    }

    /**
     * This method tries to match a string against a regular expression the current matcher represents.
     * @param target String to match
     * @return true if matches, false otherwise
     */
    public boolean match(String target) {
        return vm.match(target);
    }

    /*
   This is an approximate grammar. I sketched it to make my life easier,
   and I leave it here to make reader's life a little easier. (Maybe)
   r ::=
         re
       | re '|' r

   re ::=
         literal q? re
       | ( re ) q? re
       | [ ^? literals_list ] q? re
       | epsilon

   literal ::=
         symbol
       | \+ | \. | \? | \| | \*
       | \d | \w | .

   q? ::=
        * | + | ? | epsilon

   ^? ::= ^ | epsilon

   literals_list ::=
       | literal literals_list
       | epsilon

   */
    private static OffsetAndAST compile1(String regex, int offset, int openParens){
        int cp;
        AST currentAst = new EmptyNode();
        boolean allowPipe = false;
        AST prev = null;

        while (offset < regex.length()) {
            cp = regex.codePointAt(offset);
            // This is a hack to avoid avoiding left recursion through grammar tweaks.
            if(cp == '|') {
                if(!allowPipe) {
                    throw new IllegalStateException("Pipe cannot be the first symbol of a valid RE subexepression");
                } else {
                    OffsetAndAST right = compile1(regex, ++offset, openParens);
                    currentAst = new Alternate(currentAst, right.getTree());
                    offset = right.getOffset();
                    continue;
                }
            }

            if(cp == '(') {
                final OffsetAndAST offsetAndAST = compile1(regex, ++offset, openParens + 1);
                if(regex.codePointAt(offsetAndAST.getOffset()) != ')') {
                    throw new IllegalStateException("Expecting ), got " + cpToString(cp));
                }
                offset = offsetAndAST.getOffset() + 1;
                currentAst = offsetAndAST.getTree();
            } else if(cp == '[') {
                final OffsetAndAST offsetAndAST = alternatives(regex, ++offset);
                if(regex.codePointAt(offsetAndAST.getOffset()) != ']') {
                    throw new IllegalStateException("Expecting ], got " + cpToString(cp));
                }
                offset = offsetAndAST.getOffset() + 1;
                currentAst = offsetAndAST.getTree();
            } else if (cp == ')')  {
                if(openParens > 0) {
                    return new OffsetAndAST(offset, currentAst);
                } else {
                    throw new IllegalStateException("Unexpected )");
                }
            } else {
                final OffsetAndAST offsetAndAST = literal(regex, offset);
                offset = offsetAndAST.getOffset();
                currentAst = offsetAndAST.getTree();
            }

            if(offset >= regex.length()) {
                if(prev != null) {
                    return new OffsetAndAST(offset, new Concatenate(prev, currentAst));
                } else {
                    return new OffsetAndAST(offset, currentAst);
                }
            }

            // Check for quantifiers right away
            cp = regex.codePointAt(offset);
            AST tree = null;
            if(cp == '?') {
                tree = new Optional(currentAst);
            } else if (cp == '*') {
                tree = new Star(currentAst);
            } else if (cp == '+') {
                tree = new Plus(currentAst);
            }

            if(tree != null) {
                offset++;
                currentAst = tree;
            }

            if(prev != null) {
                currentAst = new Concatenate(prev, currentAst);
            }
            allowPipe = true;
            prev = currentAst;
        }

        return new OffsetAndAST(offset, currentAst);
    }

    private static OffsetAndAST alternatives(String regex, int offset) {
        if(regex.codePointAt(offset) == '^') {
            return literalsList(regex, offset + 1, true);
        } else {
            return literalsList(regex, offset, false);
        }
    }

    private static OffsetAndAST literalsList(String regex, int offset, boolean negate) {
        int cp = regex.codePointAt(offset);
        Collection<Integer> cps = new HashSet<>();

        while (cp != ']') {
            cps.add(cp);
            cp = regex.codePointAt(++offset);
        }

        if(negate) {
            return new OffsetAndAST(offset, new CharSetNode(new ComplementCharGroup(new SetCharGroup(cps))));
        } else {
            return new OffsetAndAST(offset, new CharSetNode(new SetCharGroup(cps)));
        }
    }

    private static OffsetAndAST literal(String regex, int offset) {
        int cp = regex.codePointAt(offset++);

        if(cp == '?' ||
                cp == '+' ||
                cp == '*' ||
                cp == '|') {
            throw new IllegalStateException("Invalid char " + cpToString(cp));
        }

        if(cp == '\\') {
            cp = regex.codePointAt(offset++);
            if(cp == 'd') {
                return new OffsetAndAST(offset, new CharSetNode(new DigitCharGroup()));
            }

            if(cp == 'w') {
                return new OffsetAndAST(offset, new CharSetNode(new LetterCharGroup()));
            }

            return new OffsetAndAST(offset, new CharSetNode(new SingleCharGroup(cp)));
        }

        if(cp == '.') {
            return new OffsetAndAST(offset, new CharSetNode(new AnyCharGroup()));
        }

        return new OffsetAndAST(offset, new CharSetNode(new SingleCharGroup(cp)));
    }

    public static String cpToString(int cp) {
        final char[] chars = Character.toChars(cp);
        return chars.length == 2 ? ("" + chars[0] + chars[1]) : ("" + chars[0]);
    }

    /**
     * A tuple class.
     */
    private static class OffsetAndAST {
        private final int offset;
        private final AST tree;

        private OffsetAndAST(int offset, AST tree) {
            this.offset = offset;
            this.tree = tree;
        }

        public int getOffset() {
            return offset;
        }

        public AST getTree() {
            return tree;
        }
    }
}
