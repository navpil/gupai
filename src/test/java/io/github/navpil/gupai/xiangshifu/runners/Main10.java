package io.github.navpil.gupai.xiangshifu.runners;

import io.github.navpil.gupai.ChineseDominoSet;
import io.github.navpil.gupai.XuanHePaiPu;
import io.github.navpil.gupai.xiangshifu.Visualizer;
import io.github.navpil.gupai.xiangshifu.XiangShiFuRules;
import io.github.navpil.gupai.xiangshifu.solution.Solver;
import io.github.navpil.gupai.xiangshifu.Triplet;

import java.util.List;

public class Main10 {

    public static void main(String[] args) {
        List<Triplet> triplets = ChineseDominoSet.random10Triplets();

        System.out.println(triplets);

        XiangShiFuRules.changeRules(XuanHePaiPu.xiangShiFuModern());

        Visualizer.visualize(new Solver(3).solve(triplets));

    }



}
