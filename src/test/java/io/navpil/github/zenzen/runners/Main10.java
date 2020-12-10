package io.navpil.github.zenzen.runners;

import io.navpil.github.zenzen.ChineseDominoSet;
import io.navpil.github.zenzen.solution.Solver;
import io.navpil.github.zenzen.Triplet;
import io.navpil.github.zenzen.Visualizer;

import java.util.List;

public class Main10 {

    public static void main(String[] args) {
        List<Triplet> triplets = ChineseDominoSet.random10Triplets();

        System.out.println(triplets);

        Visualizer.visualize(new Solver(3).solve(triplets));

    }



}
