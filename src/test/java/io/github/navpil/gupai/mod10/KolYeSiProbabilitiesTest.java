package io.github.navpil.gupai.mod10;

import io.github.navpil.gupai.ChineseDominoSet;
import io.github.navpil.gupai.dominos.Domino;
import org.assertj.core.api.Assertions;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class KolYeSiProbabilitiesTest {

    @Test
    public void pairFrequencyCalculatedCorrectly() {

        final int[] ints = KolYeSiProbabilities.calculatePairsFrequencyFor(Domino.of(3, 2), Collections.emptyList());
        final int[] bruteForce = bruteForceCalculatePairsFor(Domino.of(3, 2), Collections.emptyList());

        Assertions.assertThat(ints).isEqualTo(bruteForce);

        System.out.println(getSum(bruteForce));

    }

    @Test
    public void pairFrequencyWithExcludedDominoCalculatedCorrectly() {
        Domino dealtDomino = Domino.of(6, 5);
        Domino d1 = Domino.of(4, 1);
        Domino d2 = Domino.of(1, 1);

        final int[] ints = KolYeSiProbabilities.calculatePairsFrequencyFor(dealtDomino, List.of(d1, d2));
        final int[] bruteForce = bruteForceCalculatePairsFor(dealtDomino, List.of(d1, d2));

        System.out.println("BF sum: " + getSum(bruteForce));
        System.out.println("sum: " + getSum(ints));

        Assertions.assertThat(ints).isEqualTo(bruteForce);

    }


    @Test
    public void tripletFrequencyCalculatedCorrectly() {

        final int[] ints = KolYeSiProbabilities.calculateTripletsFrequencyFor(Domino.of(3, 2), Collections.emptyList());
        final int[] bruteForce = bruteForceCalculateTripletsFor(Domino.of(3, 2), Collections.emptyList());

        Assertions.assertThat(ints).isEqualTo(bruteForce);

        System.out.println(getSum(bruteForce));

    }

    @Test
    public void tripletFrequencyWithExcludedDominoCalculatedCorrectly() {
        Domino dealtDomino = Domino.of(6, 5);
        Domino d1 = Domino.of(4, 1);
        Domino d2 = Domino.of(1, 1);

        final int[] ints = KolYeSiProbabilities.calculateTripletsFrequencyFor(dealtDomino, List.of(d1, d2));
        final int[] bruteForce = bruteForceCalculateTripletsFor(dealtDomino, List.of(d1, d2));

        System.out.println("BF sum: " + getSum(bruteForce));
        System.out.println("sum: " + getSum(ints));

        Assertions.assertThat(ints).isEqualTo(bruteForce);

    }

    private int getSum(int[] bruteForce) {
        int sum = 0;
        for (int i : bruteForce) {
            sum += i;
        }
        return sum;
    }

    @Test
    public void showWinningProbabilityDependingOnDealtTile() {
        double [] averages = new double[10];
        for (int i = 0; i < 10; i++) {
            final int[] ints = KolYeSiProbabilities.calculateTripletsFrequencyFor(i);
            final double avg = avg(ints);
            averages[i] = avg;
            System.out.println(i + " : " + avg + ", " + Arrays.toString(ints));
        }

        for (int i = 0; i < 10; i++) {
            final int[] ints = KolYeSiProbabilities.calculatePairsFrequencyFor(i, Collections.emptyList());
            final double avg = avg(ints);
            averages[i] = Math.max(averages[i], avg);
            System.out.println(i + " : " + avg + ", " + Arrays.toString(ints));
        }

        System.out.println("Total:");
        final ArrayList<PipWithProbability> list = new ArrayList<>();
        for (int i = 0; i < averages.length; i++) {
            list.add(new PipWithProbability(i, averages[i]));
        }

        Collections.sort(list);
        for (PipWithProbability pipWithProbability : list) {
            System.out.println(pipWithProbability);
        }


    }

    public static class PipWithProbability implements Comparable<PipWithProbability> {
        private final int pip;
        private final double probability;

        public PipWithProbability(int pip, double probability) {
            this.pip = pip;
            this.probability = probability;
        }

        @Override
        public int compareTo(PipWithProbability o) {
            final int dc = Double.compare(probability, o.probability);
            if (dc != 0) {
                return dc;
            }
            return Integer.compare(pip, o.pip);
        }

        @Override
        public String toString() {
            return pip +
                    " : " + probability;
        }
    }

    public double avg(int [] ints) {
        long totalValue = 0;
        long totalCount = 0;
        for (int anInt : ints) {
            totalCount += anInt;
        }
        for (int value = 0; value < ints.length; value++) {
            final int count = ints[value];
            totalValue += value*count;
        }
        return 1.0 * totalValue / totalCount;
    }

    public static int[] bruteForceCalculatePairsFor(Domino domino, List<Domino> excluded) {
        int [] pairs = new int[10];
        final List<Domino> dominos = ChineseDominoSet.create();
        dominos.remove(domino);
        for (Domino e : excluded) {
            dominos.remove(e);
        }
        for (Domino d : dominos) {
            pairs[Mod10Calculation.mod10(domino, d)]++;
        }
        return pairs;
    }

    public static int[] bruteForceCalculateTripletsFor(Domino domino, List<Domino> excluded) {
        int [] pairs = new int[10];
        final List<Domino> dominos = ChineseDominoSet.create();
        dominos.remove(domino);
        for (Domino e : excluded) {
            dominos.remove(e);
        }
        for (int i = 0; i < dominos.size(); i++) {
            for (int j = i + 1; j < dominos.size(); j++) {
                pairs[Mod10Calculation.mod10(domino, dominos.get(i), dominos.get(j))]++;
            }
        }
        System.out.println(Arrays.toString(pairs));
        return pairs;
    }
}