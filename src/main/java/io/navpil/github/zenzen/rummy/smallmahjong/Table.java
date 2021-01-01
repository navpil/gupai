package io.navpil.github.zenzen.rummy.smallmahjong;

import io.navpil.github.zenzen.dominos.Domino;
import io.navpil.github.zenzen.util.Bag;
import io.navpil.github.zenzen.util.HashBag;

import java.util.Collection;
import java.util.HashMap;

public class Table {

    private HashBag<Domino> dominos = new HashBag<>();
    private Domino lastDiscard;
    final HashMap<String, Bag<Triplet>> winningTriplets = new HashMap<>();

    public Domino lastDiscard() {
        return lastDiscard;
    }

    public void remove(Domino wished) {
        dominos.remove(wished);
    }

    public void add(Domino discard) {
        lastDiscard = discard;
        dominos.add(discard);
    }

    public void add(String name, Triplet triplet) {
        if (!winningTriplets.containsKey(name)) {
            winningTriplets.put(name, new HashBag<>());
        }
        winningTriplets.get(name).add(triplet);
    }

    public Collection<Triplet> getTriplets(String name) {
        return winningTriplets.get(name);
    }
}
