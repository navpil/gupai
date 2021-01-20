package io.github.navpil.gupai.mod10.paigow;

import io.github.navpil.gupai.Domino;
import io.github.navpil.gupai.util.CollectionUtil;
import io.github.navpil.gupai.util.HashBag;

import java.util.Collection;

public class ComputerPlayer extends AbstractPlayer {

    private final PaiGowHandRanking ranking;

    public ComputerPlayer(String name, int money, PaiGowHandRanking ranking) {
        super(name, money);
        this.ranking = ranking;
    }

    @Override
    public Hand hand() {
        final Collection<Collection<Domino>> permutations = CollectionUtil.allPermutations(dominos, 2);
        int max = -1;
        Hand hand = null;
        final HashBag<Domino> bag = new HashBag<>(this.dominos);
        for (Collection<Domino> permutation : permutations) {
            bag.strictRemoveAll(permutation);
            final PaiGowPair p1 = new PaiGowPair(permutation);
            final PaiGowPair p2 = new PaiGowPair(bag);
            final int totalValue = ranking.getOveralRanking(p1) + ranking.getOveralRanking(p2);
            if (totalValue > max) {
                max = totalValue;
                hand = new Hand(p1, p2);
            }
            bag.addAll(permutation);
        }
        return hand;
    }
}
