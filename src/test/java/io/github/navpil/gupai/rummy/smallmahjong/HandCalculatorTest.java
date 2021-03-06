package io.github.navpil.gupai.rummy.smallmahjong;

import io.github.navpil.gupai.DominoParser;
import io.github.navpil.gupai.Domino;
import org.assertj.core.api.Assertions;
import org.junit.Test;

import java.util.List;

public class HandCalculatorTest {

    @Test
    public void calculateHands() {
        final List<Domino> dominos = DominoParser.parseList("[4:2], [6:4], [6:1], [3:2], [2:2], [4:4]");
        final List<Hand> hands = HandCalculator.calculateHands(dominos);
        System.out.println(hands);

        Assertions.assertThat(hands.get(0).triplets.contains(new Triplet(List.of(Domino.of(4,2), Domino.of(2, 2), Domino.of(4, 4)))));

    }
}
