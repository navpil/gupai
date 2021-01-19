package io.github.navpil.gupai.mod10.paigow;

import io.github.navpil.gupai.dominos.Domino;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

public class PaiGowPair {

    private final ArrayList<Domino> dominos;

    public PaiGowPair(Domino d1, Domino d2) {
        this(List.of(d1, d2));
    }

    public PaiGowPair(Collection<Domino> ds) {
        dominos = new ArrayList<>(ds);
    }

    public ArrayList<Domino> getDominos() {
        return dominos;
    }

    public Domino get(int i) {
        return dominos.get(i);
    }

    @Override
    public String toString() {
        return "P" + dominos ;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PaiGowPair that = (PaiGowPair) o;
        return Objects.equals(dominos, that.dominos);
    }

    @Override
    public int hashCode() {
        return Objects.hash(dominos);
    }
}
