package io.github.navpil.gupai.jielong.player;

import io.github.navpil.gupai.Domino;
import io.github.navpil.gupai.jielong.player.evaluators.PriorityUtil;
import io.github.navpil.gupai.jielong.player.evaluators.RarenessMoveEvaluator;
import org.assertj.core.api.Assertions;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RarenessMoveEvaluatorTest {

    @Test
    public void testMove() {

        final ArrayList<Domino> dominos = new ArrayList<>(List.of(new Domino(1, 2), new Domino(1, 2), new Domino(1, 1), new Domino(3, 4)));

        final List<Integer> priorities = new RarenessMoveEvaluator().evaluateMove(DominoTestHelper.toMoves(dominos), null, new PlayerState(dominos, Collections.emptyList()));
        final List<Domino> sorted = PriorityUtil.sort(priorities, dominos);

        Assertions.assertThat(sorted.get(0)).isEqualTo(new Domino(1, 1));
        Assertions.assertThat(sorted.get(3)).isEqualTo(new Domino(3, 4));
    }

    @Test
    public void testPutDown() {
        final ArrayList<Domino> dominos = new ArrayList<>(List.of(new Domino(1, 2), new Domino(1, 2), new Domino(1, 1), new Domino(3, 4)));

        final List<Integer> priorities = new RarenessMoveEvaluator().evaluatePutDown(DominoTestHelper.toMoves(dominos), null, new PlayerState(dominos, Collections.emptyList()));
        final List<Domino> sorted = PriorityUtil.sort(priorities, dominos);

        Assertions.assertThat(sorted.get(0)).isEqualTo(new Domino(1, 1));
        Assertions.assertThat(sorted.get(3)).isEqualTo(new Domino(3, 4));
    }



}
