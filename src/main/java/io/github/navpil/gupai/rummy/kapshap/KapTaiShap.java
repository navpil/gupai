package io.github.navpil.gupai.rummy.kapshap;

import io.github.navpil.gupai.fishing.tsungshap.RunManySimulations;
import io.github.navpil.gupai.ChineseDominoSet;
import io.github.navpil.gupai.dominos.Domino;

import java.util.ArrayList;
import java.util.List;

public class KapTaiShap {
    public static void main(String[] args) {
        List<KapShapPlayer> players = new ArrayList<>();
        for (int i = 0; i < 9; i++) {
            players.add(new ComputerKapShapPlayer("Comp-" + (i + 1)));
        }
        players.add(new RealKapShapPlayer("Jim"));
        final KapShapRuleset rules = new KapShapRuleset(KapShapRuleset.Offer.ALL, true, 4, 10, false);
        final List<Domino> deck = ChineseDominoSet.create(rules.getSetCount());

        new RunManySimulations().runManySimulations(deck, players, rules, 100, KapShap::runSimulation);
    }
}
