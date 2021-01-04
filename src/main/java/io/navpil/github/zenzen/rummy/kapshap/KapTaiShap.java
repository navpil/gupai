package io.navpil.github.zenzen.rummy.kapshap;

import io.navpil.github.zenzen.ChineseDominoSet;
import io.navpil.github.zenzen.dominos.Domino;
import io.navpil.github.zenzen.fishing.tsungshap.RunManySimulations;

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
