package io.github.navpil.gupai.runners;

import io.github.navpil.gupai.Combination;
import io.github.navpil.gupai.Triplet;
import io.github.navpil.gupai.TripletParser;
import io.github.navpil.gupai.Visualizer;
import io.github.navpil.gupai.solution.EightTripletsWinningCondition;
import io.github.navpil.gupai.solution.Solver;

import java.util.List;
import java.util.Set;

public class Main8 {

    public static void main(String[] args) {
//        final List<Triplet> triplets = ChineseDominoSet.random8Triplets();
        final List<Triplet> nonWorkingTriplets = TripletParser.parse("[Triplet{[[ - ], [2:2], [6:2]]}, Triplet{[[ - ], [3:3], [5:4]]}, Triplet{[[ - ], [5:1], [6:6]]}, Triplet{[[ - ], [1:1], [6:6]]}, Triplet{[[3:2], [4:3], [6:3]]}, Triplet{[[3:3], [6:1], [6:4]]}, Triplet{[[4:1], [4:4], [6:5]]}, Triplet{[[4:4], [5:5], [6:1]]}, Triplet{[[2:1], [6:4], [6:5]]}, Triplet{[[3:1], [5:2], [5:3]]}, Triplet{[[1:1], [2:2], [5:5]]}, Triplet{[[3:1], [4:2], [5:1]]}]");
        final List<Triplet> triplets = nonWorkingTriplets;

        System.out.println(triplets);

        final Set<Combination> military = Set.of(Combination.NINES, Combination.EIGHTS, Combination.SEVENS, Combination.FIVES);
        final Set<Combination> civil = Set.of(Combination.HEAVEN, Combination.EARTH, Combination.MAN, Combination.HARMONY);
        final EightTripletsWinningCondition wc = new EightTripletsWinningCondition(military);
        Visualizer.visualize(new Solver(2, wc).solve(triplets));





    }



}
