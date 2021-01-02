package io.navpil.github.zenzen.fishing;

import io.navpil.github.zenzen.dominos.Domino;
import io.navpil.github.zenzen.util.Bag;
import io.navpil.github.zenzen.util.HashBag;

import java.util.Collection;

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

    public Collection<Domino> getDominos() {
        final HashBag<Domino> dominos = new HashBag<>(fish);
        dominos.add(bait);
        return dominos;
    }
}
