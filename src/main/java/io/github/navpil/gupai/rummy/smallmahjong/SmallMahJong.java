package io.github.navpil.gupai.rummy.smallmahjong;

import io.github.navpil.gupai.fishing.tsungshap.RunManySimulations;
import io.github.navpil.gupai.ChineseDominoSet;
import io.github.navpil.gupai.dominos.Domino;

import java.util.List;

import static io.github.navpil.gupai.rummy.smallmahjong.HandCalculator.evaluate;

public class SmallMahJong {

    public static void main(String [] args) {
        final List<Player> players = List.of(
                new ComputerPlayer("Comp-1"),
                new ComputerPlayer("Comp-2"),
                new ComputerPlayer("Comp-3")
//                new RealPlayer("Jim")
        );

        final List<Domino> deck = ChineseDominoSet.create();
        new RunManySimulations().runManySimulations(deck, players, "NoRules", 100,
                (dominos, players1, ruleSet, whoGoesFirst) -> Simulation.runSimulation(dominos, players1, whoGoesFirst));
    }

}

