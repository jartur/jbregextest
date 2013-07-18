package me.jartur.regex.chargroups;

/**
* Created by IntelliJ IDEA.
* User: jt
* Date: 7/18/13
* Time: 11:31 PM
*/
public class ComplementCharGroup implements CharGroup {
    private final CharGroup set;

    public ComplementCharGroup(CharGroup set) {
        this.set = set;
    }

    @Override
    public boolean in(int cp) {
        return !set.in(cp);
    }
}
