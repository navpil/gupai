package io.navpil.github.zenzen.runners;

import io.navpil.github.zenzen.Combination;
import io.navpil.github.zenzen.solution.ExactPairWinningCondition;
import io.navpil.github.zenzen.solution.Solver;
import io.navpil.github.zenzen.Triplet;
import io.navpil.github.zenzen.TripletParser;
import io.navpil.github.zenzen.Visualizer;

import java.util.List;

public class MainWithParser {

    public static void main(String[] args) {
        final String s = "[Triplet{[[1:5], [5:5], [2:2]]}, Triplet{[[2:6], [4:4], [4:4]]}, Triplet{[[3:6], [1:1], [3:3]]}, Triplet{[[4:6], [1:4], [ - ]]}, Triplet{[[3:4], [5:5], [1:6]]}, Triplet{[[5:6], [1:3], [6:6]]}, Triplet{[[1:2], [4:5], [2:4]]}, Triplet{[[4:6], [5:6], [1:3]]}, Triplet{[[1:1], [1:5], [2:5]]}, Triplet{[[3:5], [6:6], [3:3]]}, Triplet{[[2:2], [2:3], [1:6]]}]";
        final String n = "[Triplet{[[5:4], [6:6], [6:4]]}, Triplet{[[4:3], [2:2], [4:4]]}, Triplet{[[5:5], [5:3], [4:2]]}, Triplet{[[6:4], [6:5], [4:4]]}, Triplet{[[5:5], [3:2], [6:1]]}, Triplet{[[3:3], [2:2], [3:3]]}, Triplet{[[3:1], [6:5], [1:1]]}, Triplet{[[6:6], [5:1], [5:2]]}, Triplet{[[6:3], [2:1], [5:1]]}, Triplet{[[ - ], [4:1], [6:1]]}, Triplet{[[6:2], [3:1], [1:1]]}]";

        final List<Triplet> triplets = TripletParser.parse(n);

        Visualizer.visualize(new Solver(1, new ExactPairWinningCondition(Combination.NINES)).solve(triplets));

    }

}
