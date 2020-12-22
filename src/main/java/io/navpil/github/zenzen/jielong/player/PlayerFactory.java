package io.navpil.github.zenzen.jielong.player;

import io.navpil.github.zenzen.dominos.Domino;
import io.navpil.github.zenzen.jielong.player.evaluators.CombiningMoveEvaluator;
import io.navpil.github.zenzen.jielong.player.evaluators.MinMaxMoveEvaluator;
import io.navpil.github.zenzen.jielong.player.evaluators.PipCounterMoveEvaluator;
import io.navpil.github.zenzen.jielong.player.evaluators.RarenessMoveEvaluator;
import io.navpil.github.zenzen.jielong.player.evaluators.SuanZhangMoveEvaluator;

import java.util.List;

public class PlayerFactory {

    public static Player createCombiningStrategyPlayer(String name, List<Domino> dominos) {
        return new PriorityPlayer(name, dominos, new CombiningMoveEvaluator()
                .addEvaluator(new MinMaxMoveEvaluator())
                .addEvaluator(new RarenessMoveEvaluator())
                .addEvaluator(new PipCounterMoveEvaluator(), 20)
                .addEvaluator(new SuanZhangMoveEvaluator(), 0));
    }

    public static Player createMinMaxPlayer(String name, List<Domino> dominos) {
        return new PriorityPlayer(name, dominos, new CombiningMoveEvaluator()
                .addEvaluator(new MinMaxMoveEvaluator())
                .addEvaluator(new PipCounterMoveEvaluator(), 20)
                .addEvaluator(new SuanZhangMoveEvaluator(), 0));
    }

    public static Player createRarenessPlayer(String name, List<Domino> dominos) {
        return new PriorityPlayer(name, dominos, new CombiningMoveEvaluator()
                .addEvaluator(new RarenessMoveEvaluator())
                .addEvaluator(new PipCounterMoveEvaluator(), 20)
                .addEvaluator(new SuanZhangMoveEvaluator(), 0));
    }

    public static Player createRandomPlayerImpl(String name, List<Domino> dominos) {
        return new PriorityPlayer(name, dominos, new CombiningMoveEvaluator());
    }
}
