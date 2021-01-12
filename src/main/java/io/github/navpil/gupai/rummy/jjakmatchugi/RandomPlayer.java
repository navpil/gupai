package io.github.navpil.gupai.rummy.jjakmatchugi;

import io.github.navpil.gupai.dominos.Domino;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Random;

public class RandomPlayer extends AbstractPlayer {

    private final Random random;

    public RandomPlayer(String name) {
        super(name);
        random = new Random();
    }

    @Override
    public Collection<Domino> offer(Domino offer) {
        final ArrayList<Domino> dominos = new ArrayList<>(this.dominos);
        dominos.add(offer);
        if (table.getRuleSet().getPairsType().validPairCombination(dominos)) {
            this.dominos.clear();
            return dominos;
        }
        return Collections.emptySet();
    }

    @Override
    public Domino discard() {
        return dominos.remove(random.nextInt(dominos.size()));
    }
}
