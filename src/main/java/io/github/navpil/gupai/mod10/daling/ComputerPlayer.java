package io.github.navpil.gupai.mod10.daling;

import io.github.navpil.gupai.Domino;
import io.github.navpil.gupai.mod10.Mod10Rule;
import io.github.navpil.gupai.util.CollectionUtil;
import io.github.navpil.gupai.util.HashBag;

import java.util.Collection;

public class ComputerPlayer extends AbstractPlayer {

    public ComputerPlayer(String name, int money) {
        super(name, money);
    }

    @Override
    public DaLingHand getHand(Table table) {
        DaLingHand hand = null;
        int bankerPoints = table.getBankerPoints();
        final Collection<Collection<Domino>> triplets = CollectionUtil.allPermutations(dominos, 3);
        final HashBag<Domino> bag = new HashBag<>(dominos);

        int totalSingleLose = table.getBets().get(getName()).getBet(0);
        int totalDoubleLose = table.getBets().get(getName()).getBet(1);
        int totalTripleLose = table.getBets().get(getName()).getBet(2);

        int totalSingleWin = table.getBets().values().stream().mapToInt(b -> b.getBet(0)).sum() - totalSingleLose;
        int totalDoubleWin = table.getBets().values().stream().mapToInt(b -> b.getBet(1)).sum() - totalDoubleLose;
        int totalTripleWin = table.getBets().values().stream().mapToInt(b -> b.getBet(2)).sum() - totalTripleLose;

        int maxTheoreticalWin = Integer.MIN_VALUE;

        for (Collection<Domino> triplet : triplets) {
            bag.strictRemoveAll(triplet);
            boolean tripletsPoints = Mod10Rule.DA_LING.getPoints(triplet).canBeExactly(bankerPoints);
            final Collection<Collection<Domino>> pairs = CollectionUtil.allPermutations(dominos, 2);
            for (Collection<Domino> pair : pairs) {
                bag.strictRemoveAll(pair);
                final Domino singleTile = bag.iterator().next();
                final boolean pairPoints = Mod10Rule.DA_LING.getPoints(pair).canBeExactly(bankerPoints);
                final boolean singleTilePoints = Mod10Rule.DA_LING.getPoints(singleTile).canBeExactly(bankerPoints);

                final int newTheoreticalWin =
                        (tripletsPoints ? totalTripleWin : -totalTripleLose)
                                + (pairPoints ? totalDoubleWin : -totalDoubleLose)
                                + (singleTilePoints ? totalSingleWin : -totalSingleLose);
                if (newTheoreticalWin > maxTheoreticalWin) {
                    maxTheoreticalWin = newTheoreticalWin;
                    hand = new DaLingHand(singleTile, pair, triplet);
                }
                bag.addAll(pair);
            }
            bag.addAll(triplet);
        }
        return hand;
    }

}
