package io.github.navpil.gupai.runners;

import io.github.navpil.gupai.xiangshifu.Visualizer;
import io.github.navpil.gupai.xiangshifu.solution.ParallelEightPairSolver;
import io.github.navpil.gupai.xiangshifu.solution.Solution;
import io.github.navpil.gupai.xiangshifu.Triplet;
import io.github.navpil.gupai.xiangshifu.TripletParser;

import java.util.List;

public class ParseEightsDemo {

    public static void main(String[] args) {
        String civilNotFound = "[Triplet{[[2:1], [5:5], [ - ]]}, Triplet{[[6:4], [6:5], [ - ]]}, Triplet{[[3:3], [6:4], [ - ]]}, Triplet{[[3:1], [4:4], [ - ]]}, Triplet{[[1:1], [5:2], [4:3]]}, Triplet{[[6:3], [6:1], [5:1]]}, Triplet{[[3:1], [5:5], [2:2]]}, Triplet{[[5:4], [6:6], [4:2]]}, Triplet{[[6:1], [3:2], [2:2]]}, Triplet{[[6:2], [4:1], [5:3]]}, Triplet{[[6:6], [4:4], [3:3]]}, Triplet{[[1:1], [6:5], [5:1]]}]";
        final List<Triplet> triplets = TripletParser.parse(civilNotFound);

        final Solution solution = new ParallelEightPairSolver().solve(triplets);
        Visualizer.visualize(solution);
//        new Solve(5, EightTripletsWinningCondition.createMilitary()).solve(triplets);

    }
}
