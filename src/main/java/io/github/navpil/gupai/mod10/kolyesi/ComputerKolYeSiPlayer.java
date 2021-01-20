package io.github.navpil.gupai.mod10.kolyesi;

import io.github.navpil.gupai.Domino;
import io.github.navpil.gupai.mod10.Mod10Rule;

import java.util.List;

public class ComputerKolYeSiPlayer implements KolYeSiPlayer {

    private final String name;

    /**
     * For which value we accept the push (for example we always have to accept the push for 9 and never for 0)
     */
    private final int pushThreshold;

    /**
     * Used in stake calculation - having Wen in hand decreases the perfect pair in the bankers hand
     */
    private final int bonusForWen;

    private int money;
    private Domino dealtDomino;

    public ComputerKolYeSiPlayer(String name, int money, int pushThreshold, int bonusForWen) {
        this.name = name;
        this.money = money;

        this.pushThreshold = pushThreshold;
        this.bonusForWen = bonusForWen;
    }

    @Override
    public int stake(Domino domino, int minStake, int maxStake) {
/*
These are the expected values for every mod10 value domino has, sorted from low to high

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

        int maxPossibleStake = Math.min(money, maxStake);

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
    public int dominoCount(Domino d1, Domino d2) {
        final int bankersMod10 = geMod10(d1, d2);

        final int[] pairs = KolYeSiProbabilities.calculatePairsFrequencyFor(dealtDomino, List.of(d1, d2));
        final int[] triplets = KolYeSiProbabilities.calculateTripletsFrequencyFor(dealtDomino, List.of(d1, d2));

        int pairsSum = 29;//sum(pairs);
        int tripletsSum = 406;//sum(triplets);

        int maxAcceptable = (bankersMod10 >= pushThreshold) ? bankersMod10 : bankersMod10 + 1;
        int acceptablePairs = 0;
        int acceptableTriplets = 0;

        for (int i = 9; i >= maxAcceptable; i--) {
            acceptablePairs += pairs[i];
            acceptableTriplets += triplets[i];
        }

        return (1.0 * acceptablePairs / pairsSum) > (1.0 * acceptableTriplets / tripletsSum) ? 1 : 2;
    }

    private int geMod10(Domino d1, Domino d2) {
        return Mod10Rule.KOL_YE_SI.getPoints(List.of(d1, d2)).getMax();
    }

    private int sum(int[] ints) {
        //Should always be the same, actually
        int total = 0;
        for (int anInt : ints) {
            total += anInt;
        }
        return total;
    }

    @Override
    public void lose(int stake) {
        money -= stake;
    }

    @Override
    public void win(int stake) {
        money += stake;
    }

    @Override
    public int getMoney() {
        return money;
    }

    @Override
    public String getName() {
        return name;
    }
}
