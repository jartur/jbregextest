package me.jartur.regex.chargroups;

/**
* Created by IntelliJ IDEA.
* User: jt
* Date: 7/18/13
* Time: 11:31 PM
*/
public class DigitCharGroup implements CharGroup {
    @Override
    public boolean in(int cp) {
        return Character.isDigit(cp);
    }
}
