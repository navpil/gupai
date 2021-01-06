package io.navpil.github.zenzen.rummy.smallmahjong;

import io.navpil.github.zenzen.ChineseDominoSet;
import io.navpil.github.zenzen.dominos.Domino;
import io.navpil.github.zenzen.fishing.tsungshap.RunManySimulations;
import io.navpil.github.zenzen.jielong.Stats;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static io.navpil.github.zenzen.rummy.smallmahjong.HandCalculator.evaluate;

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

