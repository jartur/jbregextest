package me.jartur.regex.chargroups;

/**
* Created by IntelliJ IDEA.
* User: jt
* Date: 7/18/13
* Time: 11:31 PM
*/
public class SingleCharGroup implements CharGroup {
    private final int cp;

    public SingleCharGroup(int cp) {
        this.cp = cp;
    }

    @Override
    public boolean in(int cp) {
        return cp == this.cp;
    }
}
