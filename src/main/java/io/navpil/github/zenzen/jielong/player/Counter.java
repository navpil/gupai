package io.navpil.github.zenzen.jielong.player;

import java.util.Objects;

public class Counter {
    private int count;

    public Counter() {
    }

    public Counter(int count) {
        this.count = count;
    }

    public void inc() {
        count += 1;
    }

    public void add(int a) {
        count += a;
    }

    public void set(int a) {
        count = a;
    }

    public int getCount() {
        return count;
    }

    public void dec() {
        count--;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Counter counter = (Counter) o;
        return count == counter.count;
    }

    @Override
    public int hashCode() {
        return Objects.hash(count);
    }

    @Override
    public String toString() {
        return "c:" + count;
    }
}
