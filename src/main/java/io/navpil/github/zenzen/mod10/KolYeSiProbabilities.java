package io.navpil.github.zenzen.mod10;

import io.navpil.github.zenzen.dominos.Domino;

import java.util.Collections;
import java.util.List;

public class KolYeSiProbabilities {

    public static int[] mods = new int[]{4, 2, 4, 1, 4, 2, 5, 4, 4, 2};

    public static int[] calculatePairsFrequencyFor(Domino domino, List<Domino> excluded) {
        final int currentDominoMod10 = Mod10Calculation.mod10(domino);

        return calculatePairsFrequencyFor(currentDominoMod10, excluded);
    }

    public static int[] calculatePairsFrequencyFor(int currentDominoMod10, List<Domino> excluded) {
        int[] modsCopy = new int[10];
        System.arraycopy(mods, 0, modsCopy, 0, modsCopy.length);

        modsCopy[currentDominoMod10]--;
        for (Domino d : excluded) {
            modsCopy[Mod10Calculation.mod10(d)]--;
        }

        int[] pairs = new int[10];
        for (int value = 0; value < modsCopy.length; value++) {
            int count = modsCopy[value];
            if (count > 0) {
                pairs[(currentDominoMod10 + value) % 10] += count;
            }
        }

        return pairs;
    }

    public static int[] calculateTripletsFrequencyFor(Domino domino, List<Domino> excluded) {

        final int currentDominoMod10 = Mod10Calculation.mod10(domino);

        return calculateTripletsFrequencyFor(currentDominoMod10, excluded);
    }

    public static int[] calculateTripletsFrequencyFor(int currentDominoMod10) {
        return calculateTripletsFrequencyFor(currentDominoMod10, Collections.emptyList());
    }

    public static int[] calculateTripletsFrequencyFor(int currentDominoMod10, List<Domino> excluded) {
        int[] modsCopy = new int[10];
        System.arraycopy(mods, 0, modsCopy, 0, modsCopy.length);

        modsCopy[currentDominoMod10]--;
        for (Domino d : excluded) {
            modsCopy[Mod10Calculation.mod10(d)]--;
        }

        int[] pairs = new int[10];
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
                final int index = (currentDominoMod10 + value1 + value2) % 10;
                pairs[index] += (count1 * count2);

            }
            modsCopy[value1]++;
        }
        for (int i = 0; i < 10; i++) {
            pairs[i] /= 2;
        }

        return pairs;
    }


}
