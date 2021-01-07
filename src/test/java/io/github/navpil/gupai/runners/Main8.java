package io.github.navpil.gupai.runners;

import io.github.navpil.gupai.ChineseDominoSet;
import io.github.navpil.gupai.Combination;
import io.github.navpil.gupai.Triplet;
import io.github.navpil.gupai.Visualizer;
import io.github.navpil.gupai.solution.EightTripletsWinningCondition;
import io.github.navpil.gupai.solution.Solver;

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
