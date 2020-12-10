package io.navpil.github.zenzen.runners;

import io.navpil.github.zenzen.ChineseDominoSet;
import io.navpil.github.zenzen.Combination;
import io.navpil.github.zenzen.solution.EightTripletsWinningCondition;
import io.navpil.github.zenzen.solution.Solver;
import io.navpil.github.zenzen.Triplet;
import io.navpil.github.zenzen.Visualizer;

import java.util.List;
import java.util.Set;

public class Main8 {

    public static void main(String[] args) {

        final List<Triplet> triplets = ChineseDominoSet.random8Triplets();

        System.out.println(triplets);

        final Set<Combination> military = Set.of(Combination.NINES, Combination.EIGHTS, Combination.SEVENS, Combination.FIVES);
        final Set<Combination> civil = Set.of(Combination.HEAVEN, Combination.EARTH, Combination.MAN, Combination.HARMONY);
        final EightTripletsWinningCondition wc = new EightTripletsWinningCondition(civil);
        Visualizer.visualize(new Solver(2, wc).solve(triplets));





    }



}
