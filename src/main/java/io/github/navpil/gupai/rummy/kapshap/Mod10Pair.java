package io.github.navpil.gupai.rummy.kapshap;

import io.github.navpil.gupai.Domino;

import java.util.Objects;

public class Mod10Pair implements Comparable<Mod10Pair> {

    private final Domino a;
    private final Domino b;

    public Mod10Pair(Domino a, Domino b) {
        //Smaller always first
        if (a.compareTo(b) < 0) {
            this.a = a;
            this.b = b;
        } else {
            this.a = b;
            this.b = a;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Mod10Pair pair = (Mod10Pair) o;
        return Objects.equals(a, pair.a) &&
                Objects.equals(b, pair.b);
    }

    @Override
    public int hashCode() {
        return Objects.hash(a, b);
    }

    @Override
    public String toString() {
        return "Pair{" +
                a +
                ", " + b +
                '}';
    }

    @Override
    public int compareTo(Mod10Pair o) {
        final int comp = a.compareTo(o.a);
        if (comp != 0) {
            return comp;
        }
        return b.compareTo(o.b);
    }
}
