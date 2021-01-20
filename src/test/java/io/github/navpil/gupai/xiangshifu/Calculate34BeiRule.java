package io.github.navpil.gupai.xiangshifu;

import io.github.navpil.gupai.ChineseDominoSet;
import io.github.navpil.gupai.Domino;
import io.github.navpil.gupai.IDomino;
import io.github.navpil.gupai.xiangshifu.Combination;
import io.github.navpil.gupai.xiangshifu.ZenZenRules;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

public class Calculate34BeiRule {

    public static void main(String[] args) {
        final List<Domino> dominos = ChineseDominoSet.create();
        final int size = dominos.size();
        final HashSet<List<Domino>> triplets = new HashSet<>();
        for (int first = 0; first < size; first++) {
            for (int second = first + 1; second < size; second++) {
                for (int third = second + 1; third < size; third++) {
                    triplets.add(List.of(dominos.get(first), dominos.get(second), dominos.get(third)));
                }
            }
        }

        int total = 0;
        System.out.println("SanTongSanSiBei: (should only be 39)");
        for (List<Domino> triplet : triplets) {
            if (ZenZenRules.calculateCombination(triplet) == Combination.none && is34Bei(triplet)) {
                System.out.println((++total) + "" + triplet);
            }
        }
    }

    private static int findFourRun(int[] pips) {
        for (int i = 0; i <= 2; i++) {
            if (pips[i] == pips[i + 3]) {
                return pips[i];
            }
        }
        return -1;
    }

    private static boolean is34Bei(List<Domino> triplet) {
        final int[] allPips = new int[6];
        int counter = 0;
        for (IDomino dominoe : triplet) {
            final int[] pips = dominoe.getPips();
            allPips[counter++] = pips[0];
            allPips[counter++] = pips[1];
        }
        Arrays.sort(allPips);
        if (findFourRun(allPips) > 0) {
            return false;
        }

        final int threeRun = findThreeRun(allPips);
        if (threeRun == 3 || threeRun == 4) {
            int totalSum = 0;
            for (int pip : allPips) {
                totalSum += pip;
            }
            final int remainder = totalSum - (threeRun * 3);
            return remainder < 14 && (remainder % threeRun == 0);
        }
        return false;
    }

    private static int findThreeRun(int[] pips) {
        for (int i = 0; i <= 3; i++) {
            if (pips[i] == pips[i + 2]) {
                return pips[i];
            }
        }
        return -1;
    }
}
