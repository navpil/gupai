package io.github.navpil.gupai.mod10.paigow;

import io.github.navpil.gupai.dominos.Domino;

import java.util.ArrayList;
import java.util.List;

public class Deck {

    private final List<Domino> dominos;

    public Deck(List<Domino> dominos) {
        this.dominos = new ArrayList<>(dominos);
    }

    public List<Domino> take(int count) {
        final List<Domino> sublist = this.dominos.subList(0, count);
        final ArrayList<Domino> retVal = new ArrayList<>(sublist);
        sublist.clear();
        return retVal;
    }
}
