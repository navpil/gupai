package io.github.navpil.gupai.rummy.kapshap;

import io.github.navpil.gupai.util.RunManySimulations;
import io.github.navpil.gupai.ChineseDominoSet;
import io.github.navpil.gupai.Domino;

import java.util.ArrayList;
import java.util.List;

public class KapTaiShap {
    public static void main(String[] args) {
        List<KapShapPlayer> players = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            players.add(new ComputerKapShapPlayer("Comp-" + (i + 1)));
        }
//        players.add(new RealKapShapPlayer("Jim"));
        //With 10 players:
        //gin ~20
        //classic ~14
        //culin ~ 9
        final KapShapRuleset rules = KapShapRuleset.ginKapTaiShap(4);
//        new KapShapRuleset(
//                KapShapRuleset.Offer.LAST,
//                false,
//                4, 10, false);
        final List<Domino> deck = ChineseDominoSet.create(rules.getSetCount());

        System.out.println(new RunManySimulations().runManySimulations(deck, players, rules, 100, KapShap::runSimulation));
    }
}
