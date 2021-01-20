package io.github.navpil.gupai.xiangshifu;

import io.github.navpil.gupai.xiangshifu.solution.Solution;

import java.util.List;
import java.util.Set;

public class Visualizer {

    public static void visualize(Solution solution) {
        final List<Move> moves = solution.getMoves();
        final List<Triplet> triplets = solution.getTriplets();
        final Set<BoardState> states = solution.getStates();
        boolean rememberState = states != null && !states.isEmpty();
        if (moves != null) {
            System.out.println("Got winning sequence of size " + moves.size());
            System.out.println("Depth of search " + solution.getMaxNoValueAdded());
            if (rememberState) {
                System.out.println("Checked " + states.size() + " boards");
            }
            System.out.println(moves);
            System.out.println("Starting with: ");
            System.out.println(triplets);

            for (int i = moves.size() - 1; i >= 0; i--) {
                final Move move = moves.get(i);
                System.out.println("Executing move " + move);
                move.execute();
                System.out.println("Getting: " + triplets);
            }
            for (Triplet t : triplets) {
                System.out.println("Triplet " + t + " is " + t.getCombination());
            }
        } else {
            System.out.println("Stuck on " + (rememberState ? " (checked " + states.size() + " boards)" : ""));
            System.out.println(triplets);
        }
    }
}
