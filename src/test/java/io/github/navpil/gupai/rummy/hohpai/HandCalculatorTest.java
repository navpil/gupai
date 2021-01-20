package io.github.navpil.gupai.rummy.hohpai;

import io.github.navpil.gupai.Domino;
import org.junit.Test;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.*;

public class HandCalculatorTest {

    @Test
    public void calculateStraightPoints() {
        final HandCalculator handCalculator = new HandCalculator(RuleSet.hoHpai());
        final int[] pips = new int[6];
        for (int i = 1; i <= 6; i++) {
            for (int j = 1; j <= 6; j++) {
                pips[j - 1] = (i * 10) + j;
            }
            int expectedValue;
            if (i == 2) {
                expectedValue = 5;
            } else if (i == 3 || i == 4) {
                expectedValue = 4;
            } else {
                expectedValue = 3;
            }
            System.out.println();
            assertThat(handCalculator.calculatePoints(new Hand(List.of(Domino.ofList(pips)), Collections.emptyList()))).isEqualTo(expectedValue);
        }
    }

    @Test
    public void calculateUsuals() {
        final HandCalculator handCalculator = new HandCalculator(RuleSet.hoHpai());
        assertThat(handCalculator.calculatePoints(winningHand(12, 34, 56, 33, 31, 11))).isEqualTo(2);
    }

    @Test
    public void calculateDragons() {
        /*
         - 2 Full Dragons count as 3
         - 2 Full Dragons made purely of military tiles counts as 5
         - 1 Full Dragon counts as 1
         */
        final HandCalculator handCalculator = new HandCalculator(RuleSet.hoHpai());

        //2 Full dragons should count 3
        assertThat(handCalculator.calculatePoints(winningHand(12, 34, 56, 13, 24, 56))).isEqualTo(3);

        //2 Full dragons made of military tiles entirely //The sequence 1-2, 3-6, 4-5, 1-4, 2-6, 3-5, called hol-ssang-syo (Chinese, tuk shéung tsü), "solitary double sequence," counts 5.
        assertThat(handCalculator.calculatePoints(winningHand(14, 35, 62, 12, 45, 63))).isEqualTo(5);

        assertThat(handCalculator.calculatePoints(winningHand(12, 34, 56, 13, 24, 56))).isEqualTo(3);
    }

    @Test
    public void testAllPairsAreWinningHand() {
        final HandCalculator handCalculator = new HandCalculator(RuleSet.hoHpai());
        final Collection<Hand> hands = handCalculator.handPermutations(Domino.ofList(61, 61, 55, 55, 43, 42));
        final List<Hand> collect = hands.stream().filter(Hand::isWinningHand).filter(h -> h.getCombinations().size() == 1).collect(Collectors.toList());

        assertThat(collect).isNotEmpty();
        assertThat(collect.size()).isEqualTo(1);
    }

    @Test
    public void testAllPairsCount4() {
        final HandCalculator handCalculator = new HandCalculator(RuleSet.hoHpai());
        final Collection<Hand> hands = handCalculator.handPermutations(Domino.ofList(61, 61, 55, 55, 43, 42));
        final List<Hand> collect = hands.stream().filter(Hand::isWinningHand).filter(h -> h.getCombinations().size() == 1).collect(Collectors.toList());

        assertThat(handCalculator.calculatePoints(collect.get(0))).isEqualTo(4);
    }

    @Test
    public void testMixedSpecialCase() {
        final HandCalculator handCalculator = new HandCalculator(RuleSet.hoHpai());
        assertThat(handCalculator.calculatePoints(winningHand(11, 22, 33, 11, 22, 33))).isEqualTo(3);
        assertThat(handCalculator.calculatePoints(winningHand(44, 55, 66, 44, 55, 66))).isEqualTo(3);
        assertThat(handCalculator.calculatePoints(winningHand(22, 33, 66, 22, 33, 66))).isEqualTo(3);
        //Not in Culin, but strange not to have it:
        assertThat(handCalculator.calculatePoints(winningHand(11, 22, 33, 44, 55, 66))).isEqualTo(3);
        assertThat(handCalculator.calculatePoints(winningHand(12, 31, 23, 56, 64, 45))).isEqualTo(3);

    }

    private Hand winningHand(int i, int i2, int i3, int i4, int i5, int i6) {
        return new Hand(List.of(Domino.ofList(i, i2, i3), Domino.ofList(i4, i5, i6)), Collections.emptyList());
    }

}
