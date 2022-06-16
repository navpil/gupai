package io.github.navpil.gupai.mod10.kolyesi;

import io.github.navpil.gupai.Domino;
import io.github.navpil.gupai.mod10.Mod10Rule;

/**
 * This one is only relying on the weighed probability of getting some points.
 * Technically equal to usual computer player with lowest stake 5;
 */
public class ComputerKolYeSiWeightedPlayer extends PlayerSkeletal {

    //If dealt [5:5] of [6:4] will take 1 tile (because PRECALCULATED[Mod10] == 1) etc.
    private static final int[] PRECALCULATED = new int[]{1, 1, 1, 2, 2, 2, 2, 1, 2, 1};
    public static final boolean USE_PRECALCULATED_DATA = true;

    private Domino dealtDomino;

    public ComputerKolYeSiWeightedPlayer(String name, int money) {
        super(name, money);
    }

    //This player tries not do depend on staking entirely
    //Done to check only the "domino count" strategy
    @Override
    public int stake(Domino domino, int minStake, int maxStake) {
        dealtDomino = domino;
        return (minStake + maxStake) / 2;
    }

    @Override
    public int dominoCount() {
        if (USE_PRECALCULATED_DATA) {
            return PRECALCULATED[Mod10Rule.KOL_YE_SI.getPoints(dealtDomino).getMax()];
        }

        return calculateHowManyTilesToTake();
    }

    private int calculateHowManyTilesToTake() {
        final int[] pairs = KolYeSiProbabilities.calculatePairsFrequencyFor(dealtDomino);
        final int[] triplets = KolYeSiProbabilities.calculateTripletsFrequencyFor(dealtDomino);

        //Possible pairs and possible triplets
        int pairsSum = 31;
        int tripletsSum = 465;

        double weightedPairs = 0;
        double weightedTriplets = 0;
        for (int i = 0; i < 10; i++) {
            weightedPairs += (pairs[i] * i);
            weightedTriplets += (triplets[i] * i);
        }

        return (weightedPairs / pairsSum) > (weightedTriplets / tripletsSum) ? 1 : 2;
    }

}
