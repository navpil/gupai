package io.github.navpil.gupai.util;

public class CircularInteger {

    private final int size;
    private int current;

    public CircularInteger(int size, int first) {
        this.current = first;
        this.size = size;
    }

    public int current() {
        return current;
    }

    public int next() {
        current = (current + 1) % size;
        return current;
    }

    public int prev() {
        if (current == 0) {
            current = size - 1;
        } else {
            current--;
        }
        return current;
    }
}
