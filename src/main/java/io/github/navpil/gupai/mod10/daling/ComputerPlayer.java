package io.github.navpil.gupai.mod10.daling;

import io.github.navpil.gupai.Domino;
import io.github.navpil.gupai.mod10.Mod10Rule;
import io.github.navpil.gupai.util.CollectionUtil;
import io.github.navpil.gupai.util.HashBag;

import java.util.Collection;
import java.util.List;

public class ComputerPlayer extends AbstractPlayer {

    /**
     * Used in a non-exact point match.
     * Indicates whether the difference between the points in three domino stacks should be maximized or minimized
     */
    private final boolean minimizeDiff;

    public ComputerPlayer(String name, int money) {
        this(name, money, true);
    }

    public ComputerPlayer(String name, int money, boolean miminizeDifferenceBetweenStacks) {
        super(name, money);
        this.minimizeDiff = miminizeDifferenceBetweenStacks;
    }

    @Override
    public DaLingBet placeBets() {
        return new DaLingBet(List.of(1, 1, 1));
    }

    @Override
    public DaLingHand getHand(Table table) {
        //Exact point game is pretty different to the non-exact one
        if (table.getRuleSet().isExactPointGame()) {
            return getBestHandForExactMatchGame(table);
        } else {
            return getMaximumDaLingHand();
        }
    }

    private DaLingHand getMaximumDaLingHand() {
        DaLingHand hand = null;
        final Collection<Collection<Domino>> triplets = CollectionUtil.allPermutations(dominos, 3);
        final HashBag<Domino> bag = new HashBag<>(dominos);
        int minDiff = 100;
        int maxTotal = -1;

        for (Collection<Domino> triplet : triplets) {
            bag.strictRemoveAll(triplet);
            Integer tripletsPoints = Mod10Rule.DA_LING.getPoints(triplet).getMax();
            final Collection<Collection<Domino>> pairs = CollectionUtil.allPermutations(dominos, 2);
            for (Collection<Domino> pair : pairs) {
                bag.strictRemoveAll(pair);
                final Domino singleTile = bag.iterator().next();
                final Integer pairPoints = Mod10Rule.DA_LING.getPoints(pair).getMax();
                final Integer singleTilePoints = Mod10Rule.DA_LING.getPoints(singleTile).getMax();

                final int newTotal = tripletsPoints + singleTilePoints + pairPoints;
                final int max = Math.max(Math.max(tripletsPoints, singleTilePoints), pairPoints);
                final int min = Math.min(Math.min(tripletsPoints, singleTilePoints), pairPoints);
                final int newDiff = max - min;
                if (newTotal > maxTotal) {
                    maxTotal = newTotal;
                    minDiff = newDiff;
                    hand = new DaLingHand(singleTile, pair, triplet);
                } else if (newTotal == maxTotal && (minimizeDiff && newDiff < minDiff || !minimizeDiff && newDiff > minDiff)) {
                    minDiff = newDiff;
                    hand = new DaLingHand(singleTile, pair, triplet);
                }
                bag.addAll(pair);
            }
            bag.addAll(triplet);
        }
        return hand;
    }

    private DaLingHand getBestHandForExactMatchGame(Table table) {
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
