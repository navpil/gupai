package io.github.navpil.gupai.fishing;

import io.github.navpil.gupai.dominos.Domino;
import io.github.navpil.gupai.util.Bag;
import io.github.navpil.gupai.util.HashBag;

import java.util.Collection;
import java.util.Objects;

public class Catch {
    private Domino bait;
    private Bag<Domino> fish;

    public Catch(Domino bait, Bag<Domino> fish) {
        //bait can never be null
        if (bait == null || fish == null) {
            if (bait == null) {
                throw new NullPointerException("Bait cannot be null");
            }
            throw new NullPointerException("Fish cannot be null");
        }
        this.bait = bait;
        this.fish = fish;
    }

    public Domino getBait() {
        return bait;
    }

    public Bag<Domino> getFish() {
        return fish;
    }

    @Override
    public String toString() {
        return "Catch{" +
                "bait=" + bait +
                ", fish=" + fish +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Catch aCatch = (Catch) o;
        return Objects.equals(bait, aCatch.bait) &&
                Objects.equals(fish, aCatch.fish);
    }

    @Override
    public int hashCode() {
        return Objects.hash(bait, fish);
    }

    public Collection<Domino> getDominos() {
        final HashBag<Domino> dominos = new HashBag<>(fish);
        dominos.add(bait);
        return dominos;
    }
}
