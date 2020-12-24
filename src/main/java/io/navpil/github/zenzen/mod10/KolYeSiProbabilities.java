package io.navpil.github.zenzen.mod10;

import io.navpil.github.zenzen.ChineseDominoSet;
import io.navpil.github.zenzen.dominos.Domino;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class KolYeSiProbabilities {

    public static int [] mods = new int[]{4, 2, 4, 1, 4, 2, 5, 4, 4, 2};

    public static void main(String[] args) {
        calculatePairsFor(null);
    }

    public static int[] calculatePairsFor(Domino domino) {
        return calculatePairsFor(domino, Collections.emptyList());
    }

    public static int[] calculatePairsFor(Domino domino, List<Domino> excluded) {
        final int currentDominoMod10 = Mod10Calculation.mod10(domino);

        return calculatePairsFor(currentDominoMod10);
    }

    public static int[] calculatePairsFor(int currentDominoMod10) {
        return calculatePairsFor(currentDominoMod10, Collections.emptyList());
    }

    public static int[] calculatePairsFor(int currentDominoMod10, List<Domino> excluded) {
        int [] modsCopy = new int[10];
        System.arraycopy(mods, 0, modsCopy, 0, modsCopy.length);

        modsCopy[currentDominoMod10]--;
        for (Domino d : excluded) {
            modsCopy[Mod10Calculation.mod10(d)]--;
        }

        int [] pairs = new int[10];
        for (int value = 0; value < modsCopy.length; value++) {
            int count = modsCopy[value];
            if (count > 0) {
                pairs[(currentDominoMod10 + value) % 10] += count;
            }
        }

        return pairs;
    }

    public static int[] calculateTripletsFor(Domino domino, List<Domino> excluded) {

        final int currentDominoMod10 = Mod10Calculation.mod10(domino);

        return calculateTripletsFor(currentDominoMod10, excluded);
    }

    public static int[] calculateTripletsFor(int currentDominoMod10) {
        return calculateTripletsFor(currentDominoMod10, Collections.emptyList());
    }
    public static int[] calculateTripletsFor(int currentDominoMod10, List<Domino> excluded) {
        int [] modsCopy = new int[10];
        System.arraycopy(mods, 0, modsCopy, 0, modsCopy.length);

        modsCopy[currentDominoMod10]--;
        for (Domino d : excluded) {
            modsCopy[Mod10Calculation.mod10(d)]--;
        }

        int [] pairs = new int[10];
        for (int value1 = 0; value1 < modsCopy.length; value1++) {
            int count1 = modsCopy[value1];
            if (count1 <= 0) {
                continue;
            }
            modsCopy[value1]--;
            for (int value2 = 0; value2 < modsCopy.length; value2++) {
                int count2 = modsCopy[value2];
                if (count2 <= 0) {
                    continue;
                }
                pairs[(currentDominoMod10 + value1 + value2) % 10] += (count1 * count2);
            }
            modsCopy[value1]++;
        }

        return pairs;
    }

    public static int[] bruteForceCalculatePairsFor(Domino domino) {
        int [] pairs = new int[10];
        final List<Domino> dominos = ChineseDominoSet.create();
        dominos.remove(domino);
        for (Domino d : dominos) {
            pairs[Mod10Calculation.mod10(domino, d)]++;
        }
        return pairs;
    }

    public static int[] bruteForceCalculateTripletsFor(Domino domino) {
        int [] pairs = new int[10];
        final List<Domino> dominos = ChineseDominoSet.create();
        dominos.remove(domino);
        for (Domino d : dominos) {
            final List<Domino> dominos1 = ChineseDominoSet.create();
            dominos1.remove(domino);
            dominos1.remove(d);
            for (Domino d2 : dominos1) {
                pairs[Mod10Calculation.mod10(domino, d, d2)]++;
            }
        }
        System.out.println(Arrays.toString(pairs));
        return pairs;
    }



}
