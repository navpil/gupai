package io.navpil.github.zenzen.rummy.kapshap;

import io.navpil.github.zenzen.dominos.Domino;

import java.util.Objects;

public class Ngan {

    private final Domino domino;

    public Ngan(Domino domino) {
        this.domino = domino;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Ngan ngan = (Ngan) o;
        return Objects.equals(domino, ngan.domino);
    }

    @Override
    public int hashCode() {
        return Objects.hash(domino);
    }

    public Domino getDomino() {
        return domino;
    }

    @Override
    public String toString() {
        return "Ngan{" + domino + ":" + domino + '}';
    }
}
