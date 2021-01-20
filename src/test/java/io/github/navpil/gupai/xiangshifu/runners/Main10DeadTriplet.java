package io.github.navpil.gupai.xiangshifu.runners;

import io.github.navpil.gupai.ChineseDominoSet;
import io.github.navpil.gupai.XuanHePaiPu;
import io.github.navpil.gupai.xiangshifu.DeadSets;
import io.github.navpil.gupai.xiangshifu.Triplet;
import io.github.navpil.gupai.xiangshifu.Visualizer;
import io.github.navpil.gupai.xiangshifu.XiangShiFuRules;
import io.github.navpil.gupai.xiangshifu.solution.Solver;

import java.util.List;
import java.util.stream.Collectors;

public class Main10DeadTriplet {

    public static void main(String[] args) {
        final DeadSets deadSets = new DeadSets();
        List<Triplet> triplets;
        do {
            triplets = ChineseDominoSet.random10Triplets();
        } while (triplets.stream().noneMatch(t -> deadSets.isDeadSet(t.asBag())));

        System.out.println(triplets);

        final List<Triplet> copy = triplets.stream().map(Triplet::new).collect(Collectors.toList());

        XiangShiFuRules.changeRules(XuanHePaiPu.xiangShiFuModern());

        Visualizer.visualize(new Solver(3).solve(triplets));

        XiangShiFuRules.changeRules(XuanHePaiPu.xiangShiFu());

        Visualizer.visualize(new Solver(3).solve(copy));

    }



}
