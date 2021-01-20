package io.github.navpil.gupai;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class Domino implements IDomino {

    private final int top;
    private final int bottom;

    public Domino(int top, int bottom) {
        this.top = Math.max(top, bottom);
        this.bottom = Math.min(top, bottom);
    }

    public static Domino of(int i, int i1) {
        return new Domino(i, i1);
    }

    public static Domino of(int i) {
        return of(i % 10, i / 10);
    }

    public static List<Domino> ofList(int ... ints) {
        return Arrays.stream(ints).mapToObj(Domino::of).collect(Collectors.toList());
    }

    public boolean isCivil() {
        return isDouble() || is(3, 1) || is(6, 5) || is(6,4) || is(1,6) || is(1,5);
    }

    public boolean isMilitary() {
        return !isCivil();
    }

    public boolean is(int a, int b) {
        return (a == top && b == bottom) || (a == bottom && b == top);
    }

    private boolean isDouble() {
        return top == bottom;
    }

    public int[] getPips() {
        return new int[]{top, bottom};
    }

    @Override
    public String toString() {
        return "[" + top + ':' + bottom + ']';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Domino that = (Domino) o;
        return top == that.top &&
                bottom == that.bottom;
    }

    @Override
    public int hashCode() {
        return Objects.hash(top, bottom);
    }

}
