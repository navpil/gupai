package io.github.navpil.gupai.rummy.smallmahjong;

import io.github.navpil.gupai.util.Bag;
import io.github.navpil.gupai.util.HashBag;
import io.github.navpil.gupai.Domino;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class Triplet implements Comparable<Triplet> {

    private final Domino a;
    private final Domino b;
    private final Domino c;
    private final HashBag<Domino> bag;

    public Triplet(List<Domino> triplet) {
        final ArrayList<Domino> dominos = new ArrayList<>(triplet);
        Collections.sort(dominos);
        a = dominos.get(0);
        b = dominos.get(1);
        c = dominos.get(2);
        bag = new HashBag<>(dominos);
    }

    public List<Domino> asList() {
        return List.of(a, b, c);
    }

    public Bag<Domino> asBag() {
        return bag;
    }

    @Override
    public int compareTo(Triplet o) {
        int i = a.compareTo(o.a);
        if (i != 0) {
            return i;
        }
        i = b.compareTo(o.b);
        if (i != 0) {
            return i;
        }
        i = c.compareTo(o.c);
        return i;
    }

    public boolean containsBoth(Domino d1, Domino d2) {
        final Collection<Domino> dominos = new ArrayList<>();
        dominos.add(a);
        dominos.add(b);
        dominos.add(c);

        return dominos.remove(d1) && dominos.remove(d2);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Triplet triplet = (Triplet) o;
        return Objects.equals(a, triplet.a) &&
                Objects.equals(b, triplet.b) &&
                Objects.equals(c, triplet.c);
    }

    @Override
    public int hashCode() {
        return Objects.hash(a, b, c);
    }

    @Override
    public String toString() {
        return "Triplet{" +
                "a=" + a +
                ", b=" + b +
                ", c=" + c +
                '}';
    }
}
