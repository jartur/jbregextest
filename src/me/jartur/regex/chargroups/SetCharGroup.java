package me.jartur.regex.chargroups;

import java.util.Collection;

/**
* Created by IntelliJ IDEA.
* User: jt
* Date: 7/18/13
* Time: 11:31 PM
*/
public class SetCharGroup implements CharGroup {
    private final Collection<Integer> cps;

    public SetCharGroup(Collection<Integer> cps) {
        this.cps = cps;
    }

    @Override
    public boolean in(int cp) {
        return cps.contains(cp);
    }
}
