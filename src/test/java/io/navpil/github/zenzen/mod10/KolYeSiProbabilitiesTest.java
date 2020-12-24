package io.navpil.github.zenzen.mod10;

import io.navpil.github.zenzen.dominos.Domino;
import org.assertj.core.api.Assertions;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class KolYeSiProbabilitiesTest {

    @Test
    public void calculatePairsFor() {
        final int[] ints = KolYeSiProbabilities.calculatePairsFor(Domino.of(3, 2));
        final int[] bruteForce = KolYeSiProbabilities.bruteForceCalculatePairsFor(Domino.of(3, 2));

        Assertions.assertThat(ints).isEqualTo(bruteForce);
        int sum = 0;
        for (int i : bruteForce) {
            sum+= i;
        }

        System.out.println(sum);

    }

    @Test
    public void bruteForceCalculatePairsFor() {

        double [] averages = new double[10];
        for (int i = 0; i < 10; i++) {
            final int[] ints = KolYeSiProbabilities.calculateTripletsFor(i);
            final double avg = avg(ints);
            averages[i] = avg;
            System.out.println(i + " : " + avg + ", " + Arrays.toString(ints));

        }

        for (int i = 0; i < 10; i++) {
            final int[] ints = KolYeSiProbabilities.calculatePairsFor(i);
            final double avg = avg(ints);
            averages[i] = Math.max(averages[i], avg);
            System.out.println(i + " : " + avg + ", " + Arrays.toString(ints));
        }

        System.out.println("Total:");
        final ArrayList<PipWithProbability> list = new ArrayList<>();
        for (int i = 0; i < averages.length; i++) {
//            System.out.println( i + ": " + averages[i]);
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
}