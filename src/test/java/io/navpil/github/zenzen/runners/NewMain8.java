package io.navpil.github.zenzen.runners;

import io.navpil.github.zenzen.ChineseDominoSet;
import io.navpil.github.zenzen.dominos.IDomino;
import io.navpil.github.zenzen.dominos.NoDomino;
import io.navpil.github.zenzen.solution.ParallelEightPairSolver;
import io.navpil.github.zenzen.solution.Solution;
import io.navpil.github.zenzen.Triplet;
import io.navpil.github.zenzen.Visualizer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class NewMain8 {

    public static void main(String[] args) {
        List<IDomino> dominoList = new ArrayList<>(ChineseDominoSet.create());

        Collections.shuffle(dominoList);

        List<Triplet> triplets = getTriplets(dominoList);
        System.out.println(triplets);

        final Solution solution = new ParallelEightPairSolver().solve(triplets);
        Visualizer.visualize(solution);
    }

    private static List<Triplet> getTriplets(List<IDomino> dominoList) {
        List<IDomino> triplet = new ArrayList<>();
        List<Triplet> triplets = new ArrayList<>();
        int counter = 0;
        for (IDomino domino : dominoList) {
            triplet.add(domino);
            if (counter < 4 && triplet.size() == 2) {
                counter++;
                triplet.add(NoDomino.INSTANCE);
                triplets.add(new Triplet(triplet));
                triplet = new ArrayList<>();
            } else if (triplet.size() == 3) {
                triplets.add(new Triplet(triplet));
                triplet = new ArrayList<>();
            }
        }
        return triplets;
    }


}
