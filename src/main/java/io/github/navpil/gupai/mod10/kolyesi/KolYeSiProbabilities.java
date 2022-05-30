package io.github.navpil.gupai.mod10.kolyesi;

import io.github.navpil.gupai.Domino;
import io.github.navpil.gupai.mod10.Mod10Rule;

import java.util.Collections;
import java.util.List;

public class KolYeSiProbabilities {

    public static int[] mods = new int[]{4, 2, 4, 1, 4, 2, 5, 4, 4, 2};

    /**
     * What are the probabilities of getting a pair for a given domino per points sum.
     *
     * For example if the frequency for 0 points is 2 and for 1 point is 5, resulting array will start with
     *
     * [2, 5 ..... ]
     *
     * @param domino domino to search probabilities for
     * @return array with pair frequency per sum
     */
    public static int[] calculatePairsFrequencyFor(Domino domino) {
        final int currentDominoMod10 = Mod10Rule.KOL_YE_SI.getPoints(domino).getMax();

        return calculatePairsFrequencyFor(currentDominoMod10, Collections.emptyList());
    }

    /**
     * What are the probabilities of getting a pair for a given domino per points sum.
     *
     * For example if the frequency for 0 points is 2 and for 1 point is 5, resulting array will start with
     *
     * [2, 5 ..... ]
     *
     * @param domino domino to search probabilities for
     * @param excluded dominoes not to search for a pair (unaccessible dominoes)
     * @return array with pair frequency per sum
     */
    public static int[] calculatePairsFrequencyFor(Domino domino, List<Domino> excluded) {
        final int currentDominoMod10 = Mod10Rule.KOL_YE_SI.getPoints(domino).getMax();

        return calculatePairsFrequencyFor(currentDominoMod10, excluded);
    }

    static int[] calculatePairsFrequencyFor(int currentDominoMod10, List<Domino> excluded) {
        int[] modsCopy = new int[10];
        System.arraycopy(mods, 0, modsCopy, 0, modsCopy.length);

        modsCopy[currentDominoMod10]--;
        for (Domino d : excluded) {
            modsCopy[Mod10Rule.KOL_YE_SI.getPoints(d).getMax()]--;
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

    /**
     * What are the probabilities of getting a triplet for a given domino per points sum.
     *
     * For example if the frequency for 0 points is 2 and for 1 point is 5, resulting array will start with
     *
     * [2, 5 ..... ]
     *
     * @param domino domino to search probabilities for
     * @return array with triplet frequency per sum
     */
    public static int[] calculateTripletsFrequencyFor(Domino domino) {

        final int currentDominoMod10 = Mod10Rule.KOL_YE_SI.getPoints(domino).getMax();

        return calculateTripletsFrequencyFor(currentDominoMod10, Collections.emptyList());
    }

    /**
     * What are the probabilities of getting a triplet for a given domino per points sum.
     *
     * For example if the frequency for 0 points is 2 and for 1 point is 5, resulting array will start with
     *
     * [2, 5 ..... ]
     *
     * @param domino domino to search probabilities for
     * @param excluded dominoes not to search for a triplet (unaccessible dominoes)
     * @return array with triplet frequency per sum
     */
    public static int[] calculateTripletsFrequencyFor(Domino domino, List<Domino> excluded) {

        final int currentDominoMod10 = Mod10Rule.KOL_YE_SI.getPoints(domino).getMax();

        return calculateTripletsFrequencyFor(currentDominoMod10, excluded);
    }

    public static int[] calculateTripletsFrequencyFor(int currentDominoMod10, List<Domino> excluded) {
        int[] modsCopy = new int[10];
        System.arraycopy(mods, 0, modsCopy, 0, modsCopy.length);

        modsCopy[currentDominoMod10]--;
        for (Domino d : excluded) {
            modsCopy[Mod10Rule.KOL_YE_SI.getPoints(d).getMax()]--;
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
