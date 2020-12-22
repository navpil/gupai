package io.navpil.github.zenzen.jielong;

import io.navpil.github.zenzen.ChineseDominoSet;
import io.navpil.github.zenzen.dominos.Domino;
import io.navpil.github.zenzen.jielong.player.evaluators.CombiningMoveEvaluator;
import io.navpil.github.zenzen.jielong.player.evaluators.MinMaxMoveEvaluator;
import io.navpil.github.zenzen.jielong.player.evaluators.PipCounterMoveEvaluator;
import io.navpil.github.zenzen.jielong.player.Player;
import io.navpil.github.zenzen.jielong.player.PlayerFactory;
import io.navpil.github.zenzen.jielong.player.PriorityPlayer;
import io.navpil.github.zenzen.jielong.player.evaluators.RarenessMoveEvaluator;
import io.navpil.github.zenzen.jielong.player.RealPlayer;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class KkoriputigiSimulation {

    public static void main(String[] args) {
        final List<String> names = List.of("Dima", "Computer");

        List<Function<List<Domino>, Player>> playerFactories = new ArrayList<>();

        final boolean realPlayerGame = false;
        if (realPlayerGame) {
            playerFactories.add(list -> new RealPlayer(names.get(0), list));
        } else {
            playerFactories.add(list -> PlayerFactory.createRandomPlayerImpl(names.get(0), list));
        }

        playerFactories.add(list -> new PriorityPlayer(names.get(1), list, new CombiningMoveEvaluator()
                .addEvaluator(new RarenessMoveEvaluator())
                .addEvaluator(new MinMaxMoveEvaluator())
                .addEvaluator(new PipCounterMoveEvaluator(), 11)
        ));

        final int simCount = 100;
        int whoGoesFirst = 0;
        final List<Domino> dominoSet = ChineseDominoSet.create();
        for (int i = 3; i <= 6; i++) {
            dominoSet.remove(Domino.of(i, i));
            dominoSet.remove(Domino.of(i, i));
        }

        JieLongSimulation.runSeveralSimulations(names, playerFactories, simCount, whoGoesFirst, dominoSet, new KkoriputIgiPointsResolution(KkoriputIgiPointsResolution.CalculationType.TOKENS));
    }

}
