package io.navpil.github.zenzen.rummy.kapshap;

import java.util.ArrayList;
import java.util.List;

public class KapTaiShap {
    public static void main(String[] args) {
//        final List<KapShapPlayer> players = List.of(
//                new RealKapShapPlayer("Jim"),
//                new ComputerKapShapPlayer("Second"),
//                new ComputerKapShapPlayer("Third")
//        );
        List<KapShapPlayer> players = new ArrayList<>();
        for (int i = 0; i < 9; i++) {
            players.add(new ComputerKapShapPlayer("Comp-" + (i + 1)));
        }
        players.add(new RealKapShapPlayer("Jim"));
        final KapShapRuleset rules = new KapShapRuleset(KapShapRuleset.Offer.ALL, true, 4, 10, false);

        KapShap.runSimulation(players, 0, rules);
    }
}
