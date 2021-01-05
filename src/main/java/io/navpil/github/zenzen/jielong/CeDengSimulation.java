package io.navpil.github.zenzen.jielong;

import io.navpil.github.zenzen.ChineseDominoSet;
import io.navpil.github.zenzen.dominos.Domino;
import io.navpil.github.zenzen.jielong.player.Player;
import io.navpil.github.zenzen.jielong.player.PlayerFactory;
import io.navpil.github.zenzen.jielong.player.PriorityPlayer;
import io.navpil.github.zenzen.jielong.player.RealPlayer;
import io.navpil.github.zenzen.jielong.player.evaluators.CombiningMoveEvaluator;
import io.navpil.github.zenzen.jielong.player.evaluators.MinMaxMoveEvaluator;
import io.navpil.github.zenzen.jielong.player.evaluators.PipCounterMoveEvaluator;
import io.navpil.github.zenzen.jielong.player.evaluators.RarenessMoveEvaluator;

import java.util.ArrayList;
import java.util.List;

public class CeDengSimulation {

    public static void main(String[] args) {
        final List<String> names = List.of("Dima", "Computer");

        List<Player> players = new ArrayList<>();

        final boolean realPlayerGame = false;
        if (realPlayerGame) {
            players.add(new RealPlayer(names.get(0)));
        } else {
            players.add(PlayerFactory.createRandomPlayerImpl(names.get(0)));
        }

        players.add(new PriorityPlayer("Comp1", new CombiningMoveEvaluator()
                .addEvaluator(new MinMaxMoveEvaluator())
                .addEvaluator(new PipCounterMoveEvaluator(), 11)
        ));
        players.add(new PriorityPlayer("Comp2", new CombiningMoveEvaluator()
                .addEvaluator(new RarenessMoveEvaluator())
                .addEvaluator(new PipCounterMoveEvaluator(), 11)
        ));
        players.add(new PriorityPlayer("Comp3", new CombiningMoveEvaluator()
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

        JieLongSimulation.runSeveralSimulations(
                players,
                simCount,
                whoGoesFirst,
                dominoSet,
                new CeDengRuleSet(6, CeDengRuleSet.Penalty.SIX_HEAD_SEVEN_TAIL)
        );
    }

}
