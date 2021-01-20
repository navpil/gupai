package io.github.navpil.gupai.mod10;

import io.github.navpil.gupai.Domino;
import io.github.navpil.gupai.mod10.kolyesi.KolYeSiProbabilities;
import org.assertj.core.api.Assertions;
import org.assertj.core.data.Percentage;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class ComputerKolYeSiPlayerTest {

    @Test
    public void test() {
        /*
        Why a pair was chosen in this case?

        Your money 62
        You staked 10
        Banker got [4:1], [1:1]
        You have these dominoes: [[6:5], [6:5]]
        Banker won with 7 points, against your 2 it's a loss
         */

        Domino dealtDomino = Domino.of(6, 5);
        Domino d1 = Domino.of(4, 1);
        Domino d2 = Domino.of(1, 1);
        final int bankersMod10 = Mod10Rule.KOL_YE_SI.getPoints(Arrays.asList(d1, d2)).getMax();

        final int[] pairs = KolYeSiProbabilities.calculatePairsFrequencyFor(dealtDomino, List.of(d1, d2));
        final int[] triplets = KolYeSiProbabilities.calculateTripletsFrequencyFor(dealtDomino, List.of(d1, d2));

        int pairsSum = sum(pairs);
        int tripletsSum = sum(triplets);

        Assertions.assertThat(pairsSum).isEqualTo(29);
        Assertions.assertThat(tripletsSum).isEqualTo(29 * 28 / 2);

        //Push threshold
        int minAcceptable = (bankersMod10 >= 5) ? bankersMod10 : bankersMod10 + 1;
        int acceptablePairs = 0;
        int acceptableTriplets = 0;

        final double[] pairsProbability = new double[10];
        final double[] tripletsProbability = new double[10];

        for (int i = 0; i < pairs.length; i++) {
            pairsProbability [i] = pairs[i] * 1.0 / pairsSum;
            tripletsProbability [i] = triplets[i] * 1.0 / tripletsSum;
        }

        double winByPairProbability = 0.0;
        double winByTripletProbability = 0.0;

        for (int i = minAcceptable; i < pairs.length; i++) {
            acceptablePairs += pairs[i];
            acceptableTriplets += triplets[i];

            winByPairProbability += pairsProbability[i];
            winByTripletProbability += tripletsProbability[i];
        }

        Assertions.assertThat(winByPairProbability).isCloseTo((1.0 * acceptablePairs / pairsSum), Percentage.withPercentage(0.001));

        Assertions.assertThat(winByTripletProbability).isCloseTo((1.0 * acceptableTriplets / tripletsSum), Percentage.withPercentage(0.001));

        Assertions.assertThat(winByPairProbability).isGreaterThan(winByTripletProbability);
    }

    private int sum(int[] ints) {
        //Should always be the same, actually
        int total = 0;
        for (int anInt : ints) {
            total += anInt;
        }
        return total;
    }

}
