package io.navpil.github.zenzen.runners;

import io.navpil.github.zenzen.solution.Solver;
import io.navpil.github.zenzen.Triplet;
import io.navpil.github.zenzen.TripletParser;
import io.navpil.github.zenzen.Visualizer;

import java.util.List;

public class Demo {

    public static void main(String[] args) {
        String s = "[Triplet{[[5:6], [1:3], [6:2]]}, " +
                "Triplet{[[2:1], [4:4], [5:2]]}, " +
                "Triplet{[[6:1], [5:1], [3:3]]}, " +
                "Triplet{[[6:1], [1:1], [5:5]]}, " +
                "Triplet{[[1:4], [3:4], [2:4]]}, " +
                "Triplet{[[2:2], [5:5], [5:1]]}, " +
                "Triplet{[[3:1], [4:4], [5:4]]}, " +
                "Triplet{[[5:3], [6:3], [1:1]]}, " +
                "Triplet{[[4:6], [3:3], [6:4]]}, " +
                "Triplet{[[2:2], [ - ], [6:6]]}, " +
                "Triplet{[[5:6], [3:2], [6:6]]}]";

        final List<Triplet> triplets = TripletParser.parse(s);

//        new Solve(5, MainWithParser::compareMoves, MainWithParser::hasWon).solve(triplets);
        Visualizer.visualize(new Solver(2).solve(triplets));
    }
}
