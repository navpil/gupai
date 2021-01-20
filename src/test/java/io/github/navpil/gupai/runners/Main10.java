package io.github.navpil.gupai.runners;

import io.github.navpil.gupai.ChineseDominoSet;
import io.github.navpil.gupai.xiangshifu.Visualizer;
import io.github.navpil.gupai.xiangshifu.solution.Solver;
import io.github.navpil.gupai.xiangshifu.Triplet;

import java.util.List;

public class Main10 {

    public static void main(String[] args) {
        List<Triplet> triplets = ChineseDominoSet.random10Triplets();

        System.out.println(triplets);

        Visualizer.visualize(new Solver(3).solve(triplets));

    }



}
