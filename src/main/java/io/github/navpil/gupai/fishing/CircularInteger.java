package io.github.navpil.gupai.fishing;

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

    public void next() {
        current = (current + 1) % size;
    }
}
