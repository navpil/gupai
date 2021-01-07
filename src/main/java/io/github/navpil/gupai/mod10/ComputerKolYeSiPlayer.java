package io.github.navpil.gupai.mod10;

import io.github.navpil.gupai.dominos.Domino;

import java.util.List;

public class ComputerKolYeSiPlayer implements KolYeSiPlayer {

    private final String name;
    private final int pushThreshold;
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

        final int mod10 = Mod10Calculation.mod10(domino);
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

    @Override
    public int dominoCount(Domino d1, Domino d2) {
        final int bankersMod10 = Mod10Calculation.mod10(d1, d2);

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
