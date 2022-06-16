package io.github.navpil.gupai.rummy.smallmahjong;

import io.github.navpil.gupai.Domino;
import io.github.navpil.gupai.util.Bag;
import io.github.navpil.gupai.util.HashBag;

import java.util.Collection;
import java.util.HashMap;

public class Table {

    final HashMap<String, Bag<Triplet>> winningTriplets = new HashMap<>();
    private HashBag<Domino> dominos = new HashBag<>();
    private Domino lastDiscard;

    public HashBag<Domino> getAllVisibleDominoes() {
        HashBag<Domino> visible = new HashBag<>();

        visible.addAll(dominos);
        for (Bag<Triplet> value : winningTriplets.values()) {
            for (Triplet triplet : value) {
                visible.addAll(triplet.asBag());
            }
        }
        return visible;
    }

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
        return winningTriplets.getOrDefault(name, HashBag.of());
    }
}
