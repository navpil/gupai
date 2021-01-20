package io.github.navpil.gupai.jielong;

import io.github.navpil.gupai.jielong.player.evaluators.PipCounterMoveEvaluator;
import io.github.navpil.gupai.ChineseDominoSet;
import io.github.navpil.gupai.Domino;
import io.github.navpil.gupai.jielong.player.Player;
import io.github.navpil.gupai.jielong.player.PlayerFactory;
import io.github.navpil.gupai.jielong.player.PriorityPlayer;
import io.github.navpil.gupai.jielong.player.RealPlayer;
import io.github.navpil.gupai.jielong.player.evaluators.CombiningMoveEvaluator;
import io.github.navpil.gupai.jielong.player.evaluators.MinMaxMoveEvaluator;
import io.github.navpil.gupai.jielong.player.evaluators.RarenessMoveEvaluator;

import java.util.ArrayList;
import java.util.List;

public class KkoriputigiSimulation {

    public static void main(String[] args) {
        final List<String> names = List.of("Dima", "Computer");

        List<Player> players = new ArrayList<>();

        final boolean realPlayerGame = false;
        if (realPlayerGame) {
            players.add(new RealPlayer(names.get(0)));
        } else {
            players.add(PlayerFactory.createRandomPlayerImpl(names.get(0)));
        }

        players.add(new PriorityPlayer(names.get(1), new CombiningMoveEvaluator()
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

        JieLongSimulation.runSeveralSimulations(players, simCount, whoGoesFirst, dominoSet, new KkoriputIgiRuleSet(KkoriputIgiRuleSet.CalculationType.TOKENS));
    }

}
