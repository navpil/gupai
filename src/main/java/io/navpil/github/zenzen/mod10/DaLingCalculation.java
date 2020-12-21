package io.navpil.github.zenzen.mod10;

import io.navpil.github.zenzen.ChineseDominoSet;
import io.navpil.github.zenzen.dominos.Domino;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public class DaLingCalculation {

    public static void main(String[] args) {
        final List<Domino> dominos = ChineseDominoSet.create();
        final int size = dominos.size();

        int [] singles = new int[11];
        int [] doubles = new int[11];
        int [] triplets = new int[11];

        for (int i = 0; i < size; i++) {
            final Domino first = dominos.get(i);
            singles[mod10Points(first)]++;
            for (int j = i + 1; j < size; j++) {
                final Domino second = dominos.get(j);
                doubles[mod10Points(first, second)]++;
                for (int k = j + 1; k < size; k++) {
                    triplets[mod10Points(first, second, dominos.get(k))]++;
                }
            }
        }
        System.out.println(Arrays.toString(singles));//32
        System.out.println(Arrays.toString(doubles));//32*31
        System.out.println(Arrays.toString(triplets));//32*31*30

        showWinLosePercentage(singles, doubles, "Singles", "Doubles");
        showWinLosePercentage(triplets, doubles, "Triplets", "Doubles");
        showWinLosePercentage(doubles, doubles, "Doubles", "Doubles");

//        int[] transitiveA = new int[] {0, 2, 0, 1, 0, 2, 1};
//        int[] transitiveB = new int[] {0, 0, 1, 2, 2, 1, 0};
//        int[] transitiveC = new int[] {0, 1, 2, 0, 1, 0, 2};
//        showWinLosePercentage(transitiveA, transitiveB, "A", "B");
//        showWinLosePercentage(transitiveB, transitiveC, "B", "C");
//        showWinLosePercentage(transitiveA, transitiveC, "A", "C");
//
//        System.out.println(17.0 / 36.0);
//        System.out.println(4.0 / 36.0);
//        System.out.println(15.0 / 36.0);
//        System.out.println(calculatePushes(triplets, doubles));


    }

    private static void showWinLosePercentage(int[] singles, int[] doubles, String name1, String name2) {
        final double pushes = calculatePushes(singles, doubles);
        final double firstWins = calculateWins(singles, doubles);
        final double secondWins = calculateWins(doubles, singles);

        System.out.println(name1 + " pushes with " + name2 + ": " + pushes);
        System.out.println(name1 + " wins over " + name2 + ": " + firstWins);
        System.out.println(name1 + " loses to " + name2 + ": " + secondWins);

        System.out.println(pushes + firstWins + secondWins);

    }

    private static double calculateWins(int[] wins, int[] loses) {
        int winsSize = getTotal(wins);
        int losesSize = getTotal(loses);

        double winPercentage = 0;
        for (int i = wins.length - 1; i > 0; i--) {
            for (int j = i - 1; j > 0; j--) {
                final int winTiles = wins[i];
                final int loseTiles = loses[j];

                winPercentage += ((winTiles * 1.0 / winsSize) * (loseTiles * 1.0 / losesSize));
            }
        }
        return winPercentage;
    }

    private static double calculatePushes(int[] first, int[] second) {
        int firstSize = getTotal(first);
        int secondSize = getTotal(second);

        double percentage = 0;
        for (int i = 1; i < first.length; i++) {
            final int tiles1 = first[i];
            final int tiles2 = second[i];

            percentage += ((tiles1 * 1.0 / firstSize) * (tiles2 * 1.0 / secondSize));
        }
        return percentage;
    }

    private static int getTotal(int[] zeroes) {
        int firstSize = 0;
        for (int i = 1; i < zeroes.length; i++) {
            firstSize += zeroes[i];
        }
        return firstSize;
    }

    private static int mod10Points(Domino ... dominos) {
        Points points = Points.create();
        for (Domino domino : dominos) {
            points = points.with(domino);
        }
        return points.resolve();
    }

    private static int mod10Points_old(Domino ... dominos) {
        int sum = 0;
        for (Domino domino : dominos) {
            sum += domino.getPips()[0] + domino.getPips()[1];
        }
        sum = sum % 10;
        return sum == 0 ? 10 : sum;
    }

    private static class Points {

        private static final Set<Domino> geeGoon = Set.of(Domino.of(4,2), Domino.of(2,1));
        private final List<Integer> sums;

        private Points(List<Integer> sums) {
            this.sums = sums;
        }

        public static Points create() {
            return new Points(List.of(0));
        }

        public static Points create(Domino domino) {
            return new Points(getPoints(domino));
        }

        private static List<Integer> getPoints(Domino domino) {
            if (isGeeGoon(domino)) {
                return List.of(3, 6);
            } else {
                return List.of(domino.getPips()[0] + domino.getPips()[1]);
            }

        }

        public Points with(Domino domino) {
            final ArrayList<Integer> newSum = new ArrayList<>();
            for (Integer sum : this.sums) {
                for (Integer oldPoints : getPoints(domino)) {
                    newSum.add(sum + oldPoints);
                }
            }
            return new Points(newSum);
        }

        public Integer resolve() {
            return sums.stream().map(i -> i % 10).map(i -> i == 0 ? 10 : i).max(Integer::compareTo).orElseThrow();
        }

        private static boolean isGeeGoon(Domino domino) {
            return geeGoon.contains(domino);
        }

    }

}
