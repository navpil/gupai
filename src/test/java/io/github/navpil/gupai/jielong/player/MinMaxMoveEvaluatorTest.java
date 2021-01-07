package io.github.navpil.gupai.jielong.player;

import io.github.navpil.gupai.jielong.Move;
import io.github.navpil.gupai.dominos.Domino;
import io.github.navpil.gupai.jielong.player.evaluators.MinMaxMoveEvaluator;
import io.github.navpil.gupai.jielong.player.evaluators.PriorityUtil;
import org.assertj.core.api.Assertions;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MinMaxMoveEvaluatorTest {

    @Test
    public void evaluateMove() {
        final ArrayList<Domino> dominos = new ArrayList<>(List.of(new Domino(2, 1), new Domino(3, 5), new Domino(1, 1)));
        final List<Integer> movePriorities = new MinMaxMoveEvaluator().evaluateMove(DominoTestHelper.toMoves(dominos), null, null);

        final List<Domino> sorted = PriorityUtil.sort(movePriorities, dominos);

        Assertions.assertThat(sorted.get(0)).isEqualTo(new Domino(3, 5));
        Assertions.assertThat(sorted.get(2)).isEqualTo(new Domino(1,1));
    }

    @Test
    public void evaluatePutDown() {
        final ArrayList<Domino> dominos = new ArrayList<>(List.of(new Domino(2, 1), new Domino(3, 5), new Domino(1, 1)));
        final List<Integer> movePriorities = new MinMaxMoveEvaluator().evaluatePutDown(dominos.stream().map(d -> new Move(0, d.getPips()[0], d.getPips()[1])).collect(Collectors.toList()), null, null);

        final List<Domino> sorted = PriorityUtil.sort(movePriorities, dominos);

        Assertions.assertThat(sorted.get(0)).isEqualTo(new Domino(1,1));
        Assertions.assertThat(sorted.get(2)).isEqualTo(new Domino(3, 5));
    }
}