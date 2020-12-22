package io.navpil.github.zenzen.jielong.player;

import io.navpil.github.zenzen.DominoFactory;
import io.navpil.github.zenzen.dominos.Domino;
import io.navpil.github.zenzen.jielong.Move;
import io.navpil.github.zenzen.jielong.player.evaluators.CombiningMoveEvaluator;
import io.navpil.github.zenzen.jielong.player.evaluators.MinMaxMoveEvaluator;
import io.navpil.github.zenzen.jielong.player.evaluators.PriorityUtil;
import io.navpil.github.zenzen.jielong.player.evaluators.RarenessMoveEvaluator;
import org.assertj.core.api.Assertions;
import org.junit.Test;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class CombiningMoveEvaluatorTest {

    @Test
    public void evaluateMove() {
        final CombiningMoveEvaluator me = new CombiningMoveEvaluator()
                .addEvaluator(new MinMaxMoveEvaluator())
                .addEvaluator(new RarenessMoveEvaluator());

        final List<Domino> dominos = DominoFactory.parseList("[6:6], [6:6], [4:4], [4:4], [3:1], [3:1]");
        final List<Integer> integers = me.evaluateLead(dominos.stream().map(Move::lead).collect(Collectors.toList()), null, new PlayerState(dominos, Collections.emptyList()));

        System.out.println(integers);

        final List<Domino> sort = PriorityUtil.sort(integers, dominos);
        Assertions.assertThat(sort.get(0)).isEqualTo(Domino.of(6,6));

    }

    @Test
    public void evaluateMoveSameAsMinMax() {
        final CombiningMoveEvaluator me = new CombiningMoveEvaluator()
                .addEvaluator(new MinMaxMoveEvaluator())
                .addEvaluator(new RarenessMoveEvaluator(), 0);

        final List<Domino> dominos = DominoFactory.parseList("[6:6], [6:6], [4:4], [4:4], [4:1], [3:1]");
        final List<Integer> integers = me.evaluateLead(dominos.stream().map(Move::lead).collect(Collectors.toList()), null, new PlayerState(dominos, Collections.emptyList()));

        final List<Domino> sort = PriorityUtil.sort(integers, dominos);
        Assertions.assertThat(sort.get(0)).isEqualTo(Domino.of(6,6));

    }

    @Test
    public void evaluateMoveSameAsRarenes() {
        final CombiningMoveEvaluator me = new CombiningMoveEvaluator()
                .addEvaluator(new MinMaxMoveEvaluator(), 0)
                .addEvaluator(new RarenessMoveEvaluator());

        final List<Domino> dominos = DominoFactory.parseList("[6:6], [6:6], [4:4], [4:4], [4:1], [3:1]");
        final List<Integer> integers = me.evaluateLead(dominos.stream().map(Move::lead).collect(Collectors.toList()), null, new PlayerState(dominos, Collections.emptyList()));
        System.out.println(integers);

        final List<Domino> sort = PriorityUtil.sort(integers, dominos);
        Assertions.assertThat(sort.get(0)).isEqualTo(Domino.of(4,4));
    }
}