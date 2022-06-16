package io.github.navpil.gupai.mod10.kolyesi;

import io.github.navpil.gupai.ChineseDominoSet;
import io.github.navpil.gupai.Domino;
import io.github.navpil.gupai.mod10.Mod10Rule;
import org.junit.Ignore;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Ignore("develop only")
public class ComputerKolYeSiWeightedPlayerTest {

    /**
     * Check what will be the domino count for computer players
     */
    @Test
    public void testProbabilities() {
        ComputerKolYeSiWeightedPlayer p = new ComputerKolYeSiWeightedPlayer("Test", 10);
        ArrayList<ComputerKolYeSiPlayer> players = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            players.add(new ComputerKolYeSiPlayer("Test" + i, 10, i, 0));
        }

        int[] dominosToGet = new int[11];
        int[] dominosToGetWeighted = new int[11];

        List<Domino> dominos = ChineseDominoSet.create();
        for (Domino domino : dominos) {
            p.stake(domino, 10, 10);
            dominosToGetWeighted[Mod10Rule.KOL_YE_SI.getPoints(domino).getMax()] = p.dominoCount();
        }
        System.out.println("Neway:" + Arrays.toString(dominosToGetWeighted));
        for (ComputerKolYeSiPlayer player : players) {
            for (Domino domino : dominos) {
                player.stake(domino, 10, 10);
                dominosToGet[Mod10Rule.KOL_YE_SI.getPoints(domino).getMax()] = player.dominoCount();
            }
            if (Arrays.equals(dominosToGetWeighted, dominosToGet)) {
                System.out.println("BINGO! - same as weighted");
            }
            System.out.println(player.getName() + ":" + Arrays.toString(dominosToGet));
        }


    }

    //Taken from previous test
    private static final int[] PRECALCULATED = new int[]{1, 1, 1, 2, 2, 2, 2, 1, 2, 1};

    @Test
    public void showProbabilitiesForDominoMod10() {
        //Possible pairs and possible triplets
        int pairsSum = 31;
        int tripletsSum = 465;

        for (int i = 0; i < PRECALCULATED.length; i++) {
            int tiles = PRECALCULATED[i];
            if (tiles == 2) {
                int[] ints = KolYeSiProbabilities.calculateTripletsFrequencyFor(i, Collections.emptyList());
                double totalSum = 0;
                for (int j = 0; j < ints.length; j++) {
                    totalSum += ints[j] * j;
                }
                System.out.println(i + ": " + (totalSum / tripletsSum) + " (triplets)");
            } else {
                int[] ints = KolYeSiProbabilities.calculatePairsFrequencyFor(i, Collections.emptyList());
                double totalSum = 0;
                for (int j = 0; j < ints.length; j++) {
                    totalSum += ints[j] * j;
                }
                System.out.println(i + ": " + (totalSum / pairsSum) + " (pairs)");
            }

        }
    }
}