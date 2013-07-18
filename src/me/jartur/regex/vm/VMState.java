package me.jartur.regex.vm;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

public class VMState {
    final private CharSequence string;
    private final Map<Integer, Integer> pcMap = new HashMap<>();
    private Integer sp = -1;
    private int lastThreadId = 0;
    private final Queue<Integer> enqueuedThreads = new LinkedList<>();

    public VMState(CharSequence string) {
        this.string = string;
        pcMap.put(++lastThreadId, 0);
        enqueuedThreads.add(lastThreadId);
    }

    private int getCodePointAt(int index) {
        if(index >=string.length())
            return -1;

        return Character.codePointAt(string, index);
    }

    public int getCurrentCodePoint() {
        return getCodePointAt(sp);
    }

    public void incrementPc(int thread) {
        Integer current = pcMap.get(thread);
        pcMap.put(thread, (current == null ? 0 : current) + 1);
    }

    public Integer getPc(int thread) {
        return pcMap.get(thread);
    }

    public void split(int thread, int x, int y) {
        jump(thread, x);
        boolean foundThread = false;
        for (Integer integer : pcMap.values()) {
            if(integer.equals(y)) {
                foundThread = true;
                break;
            }
        }

        if(!foundThread) {
            jump(++lastThreadId, y);
            enqueuedThreads.add(lastThreadId);
        }
    }

    public void stop(int threadId) {
        pcMap.remove(threadId);
    }

    public void jump(int thread, int pc){
        pcMap.put(thread, pc);
    }

    public boolean incrementSp() {
        if(sp == -1) {
            sp = 0;
            return true;
        }

        if(Character.isSupplementaryCodePoint(getCurrentCodePoint())){
            sp += 2;
        } else {
            sp += 1;
        }

        if(sp > string.length()) {
            return false;
        }

        enqueuedThreads.addAll(pcMap.keySet());
        return true;
    }

    public Integer getNextThread() {
        if(enqueuedThreads.isEmpty())
            return null;

        return enqueuedThreads.remove();
    }
}