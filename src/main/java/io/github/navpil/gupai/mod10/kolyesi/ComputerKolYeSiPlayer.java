package io.github.navpil.gupai.mod10.kolyesi;

import io.github.navpil.gupai.Domino;
import io.github.navpil.gupai.mod10.Mod10Rule;

public class ComputerKolYeSiPlayer extends PlayerSkeletal {

    /**
     * For which value we accept the push (for example we always have to accept the push for 9 and never for 0)
     */
    private final int minAcceptable;

    /**
     * Used in stake calculation - having Wen in hand decreases the perfect pair in the bankers hand
     */
    private final int bonusForWen;

    private Domino dealtDomino;

    public ComputerKolYeSiPlayer(String name, int money, int minAcceptable, int bonusForWen) {
        super(name, money);

        this.minAcceptable = minAcceptable;
        this.bonusForWen = bonusForWen;
    }

    @Override
    public int stake(Domino domino, int minStake, int maxStake) {
/*
These are the expected values for every mod10 value domino has, sorted from low to high

Double-checked - calculated correctly, takes pair/triplet best combination into an account

 6 : 4.451612903225806
 8 : 4.451612903225806
 4 : 4.473118279569892
 5 : 4.548387096774194
 3 : 4.612903225806452
 0 : 4.741935483870968
 2 : 4.741935483870968
 7 : 4.741935483870968
 9 : 4.741935483870968
 1 : 5.064516129032258
 */
        this.dealtDomino = domino;
        int bonus = domino.isCivil() ? bonusForWen : 0;

        int step = (maxStake - minStake) / 4;

        int maxPossibleStake = Math.min(getMoney(), maxStake);

        /*
        Arbitrary choices
         */
        final int mod10 = getMod10(domino);
        switch (mod10) {
            case 6: case 8: case 4: return Math.min(minStake + bonus, maxPossibleStake);
            case 5: return Math.min(minStake + step + bonus, maxPossibleStake);
            case 3: return Math.min(minStake + (2 * step) + bonus, maxPossibleStake);
            case 0: case 2: case 7: case 9: return Math.min(minStake + ((3 * step)) + bonus, maxPossibleStake);
            case 1: return maxPossibleStake;
        }
        //Should never happen
        return minStake;
    }

    private int getMod10(Domino domino) {
        return Mod10Rule.KOL_YE_SI.getPoints(domino).getMax();
    }

    @Override
    public int dominoCount() {

        final int[] pairs = KolYeSiProbabilities.calculatePairsFrequencyFor(dealtDomino);
        final int[] triplets = KolYeSiProbabilities.calculateTripletsFrequencyFor(dealtDomino);

        int pairsSum = 31;//sum(pairs);
        int tripletsSum = 465;//sum(triplets);

        int acceptablePairs = 0;
        int acceptableTriplets = 0;

        //Maybe not the best calculation available.
        //We simply say what we still accept, and which pairs are acceptable for us
        //For instance if we still accept 5, we check how many pairs are acceptable as 5 and how many triplets
        //It would be better to somehow differentiate possibilites when there are 10 pairs for 9 and 1 for 8 and vice versa.
        for (int i = 9; i >= minAcceptable; i--) {
            acceptablePairs += pairs[i];
            acceptableTriplets += triplets[i];
        }

        return (1.0 * acceptablePairs / pairsSum) > (1.0 * acceptableTriplets / tripletsSum) ? 1 : 2;
    }

    //Should always be the same, actually
    private int sum(int[] ints) {
        int total = 0;
        for (int anInt : ints) {
            total += anInt;
        }
        return total;
    }

}
