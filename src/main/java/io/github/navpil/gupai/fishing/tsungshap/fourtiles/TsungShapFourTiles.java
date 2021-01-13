package io.github.navpil.gupai.fishing.tsungshap.fourtiles;

import io.github.navpil.gupai.ChineseDominoSet;
import io.github.navpil.gupai.dominos.Domino;
import io.github.navpil.gupai.fishing.tsungshap.NamedPlayer;
import io.github.navpil.gupai.fishing.tsungshap.RunManySimulations;
import io.github.navpil.gupai.fishing.tsungshap.TsungShap;
import io.github.navpil.gupai.fishing.tsungshap.TsungShapRuleSet;
import io.github.navpil.gupai.jielong.Stats;

import java.util.List;

/**
 * TsungShap described by SpielDomino website suggests that each player should have four tiles in a hand and can choose
 * the move to make.
 */
public class TsungShapFourTiles {

    public static void main(String[] args) {
        final List<Domino> deck = ChineseDominoSet.create();
        final List<Player> players = List.of(
                new ComputerPlayer("Comp-1"),
//                new TsungShapFourTilesComputerPlayer("Comp-2")
                new HumanPlayer("Mark")
        );
        Stats stats = new RunManySimulations().runManySimulations(
                deck,
                players,
                TsungShapRuleSet.spieldomino(),
                10,
                TsungShap::runFourTilesSimulation);
        for (NamedPlayer player : players) {
            System.out.println(player.getName() + " got points: " + stats.getPointsFor(player.getName()));
        }
    }
}
