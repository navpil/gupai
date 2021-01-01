package io.navpil.github.zenzen.jielong.player;

import io.navpil.github.zenzen.ChineseDominoSet;
import io.navpil.github.zenzen.DominoParser;
import io.navpil.github.zenzen.dominos.Domino;
import io.navpil.github.zenzen.jielong.Dragon;
import io.navpil.github.zenzen.jielong.Move;
import io.navpil.github.zenzen.jielong.NoopSuanZhang;
import io.navpil.github.zenzen.jielong.TableVisibleInformation;
import io.navpil.github.zenzen.jielong.player.evaluators.MoveEvaluator;
import io.navpil.github.zenzen.jielong.player.evaluators.PipCounterMoveEvaluator;
import io.navpil.github.zenzen.jielong.player.evaluators.PriorityUtil;
import org.assertj.core.api.Assertions;
import org.junit.Test;

import java.util.Collections;
import java.util.List;

public class PipCounterMoveEvaluatorTest {

    @Test
    public void evaluateMove() {

        /*
Dima [6:6][6:5][1:1][5:5][2:2][5:5]
MinMax [6:4][6:1][2:6][2:2][3:1][4:4]
Rare [1:6][5:1][1:1][3:3][6:3][4:4]
Combine [4:6][6:6][1:5][5:6][3:3][3:1]

Dima starts
Dragon [[6:6]]
Player MinMax played Move{side=1, inwards=6, outwards=4}
Dragon [[6:6], [6:4]]
Player Rare played Move{side=2, inwards=6, outwards=1}
Dragon [[1:6], [6:6], [6:4]]
Player Combine played Move{side=1, inwards=4, outwards=6}
Dragon [[1:6], [6:6], [6:4], [4:6]]
Player Dima played Move{side=1, inwards=6, outwards=5}
         */

        final Dragon dragon = new Dragon(Dragon.OpenArms.DOUBLE, ChineseDominoSet.dingNiuSet(), new NoopSuanZhang());
        dragon.executeMove(Move.lead(Domino.of(6, 6)));
        dragon.executeMove(new Move(1, 6, 4));

        final List<Domino> dominos = DominoParser.parseList("[1:6][5:1][1:1][3:3][6:3][4:4]".replace("][", "], ["));

        final MoveEvaluator evaluator = new PipCounterMoveEvaluator();

        final List<Move> moves = MoveFinder.getAvailableMoves(dragon.getOpenEnds(), dominos);
        final List<Integer> priorities = evaluator.evaluateMove(moves, new TableVisibleInformation(dragon), new PlayerState(dominos, Collections.emptyList()));
        final List<Move> sorted = PriorityUtil.sort(priorities, moves);
        Assertions.assertThat(sorted.get(0).getDomino()).isEqualTo(Domino.of(4,4));

    }
}