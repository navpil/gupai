package io.navpil.github.zenzen.dominos;

import java.util.Objects;

public class Pair {

    private final Domino a;
    private final Domino b;

    public Pair(Domino a, Domino b) {
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
        Pair pair = (Pair) o;
        return Objects.equals(a, pair.a) &&
                Objects.equals(b, pair.b);
    }

    @Override
    public int hashCode() {
        return Objects.hash(a, b);
    }
}
