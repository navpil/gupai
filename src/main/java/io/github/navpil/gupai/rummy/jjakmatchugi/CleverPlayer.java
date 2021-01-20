package io.github.navpil.gupai.rummy.jjakmatchugi;

import io.github.navpil.gupai.Domino;
import io.github.navpil.gupai.Pairs;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

public class CleverPlayer extends AbstractPlayer {
    public CleverPlayer(String name) {
        super(name);
    }

    @Override
    public Collection<Domino> offer(Domino offer) {
        final Pairs pairsType = table.getRuleSet().getPairsType();
        for (Domino domino : dominos) {
            final List<Domino> combination = List.of(domino, offer);
            if (pairsType.validAllPairsCombination(combination)) {
                dominos.remove(domino);
                return combination;
            }
        }
        return Collections.emptyList();
    }

    @Override
    public Domino discard() {
        final HashSet<Domino> deadTiles = new HashSet<>(table.allDeadTiles());
        final Pairs pairsType = table.getRuleSet().getPairsType();
        for (Domino domino : this.dominos) {
            if (deadTiles.contains(pairsType.other(domino))) {
                return removed(domino);
            }
        }
        return dominos.remove(dominos.size() - 1);
    }
}
