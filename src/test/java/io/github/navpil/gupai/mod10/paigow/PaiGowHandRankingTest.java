package io.github.navpil.gupai.mod10.paigow;

import io.github.navpil.gupai.Domino;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class PaiGowHandRankingTest {

    @Test
    public void testDominoComparison() {
        final PaiGowHandRanking comparator = PaiGowHandRanking.SUPREME_SIX_HIGH;
        assertThat(comparator.compareDominoes(Domino.of(66), Domino.of(11))).isEqualTo(1);
    }

    @Test
    public void testPairComparison() {
        final PaiGowHandRanking comparator = PaiGowHandRanking.SUPREME_SIX_HIGH;
        assertThat(comparator.comparePairs(
                new PaiGowPair(Domino.ofList(54, 11)),
                new PaiGowPair(Domino.ofList(54, 66))
        )).isEqualTo(-1);
    }

    @Test
    public void tesMod10Comparison() {
        final PaiGowHandRanking comparator = PaiGowHandRanking.SUPREME_SIX_HIGH;
        assertThat(comparator.comparePairs(
                new PaiGowPair(Domino.ofList(66, 43)),
                new PaiGowPair(Domino.ofList(11, 52))
        )).isEqualTo(1);
    }

    @Test
    public void tesSupremeIsHigh() {
        final PaiGowHandRanking comparator = PaiGowHandRanking.SUPREME_SIX_HIGH;
        assertThat(comparator.comparePairs(
                new PaiGowPair(Domino.ofList(66, 43)),
                new PaiGowPair(Domino.ofList(42, 21))
        )).isEqualTo(-1);
    }

    @Test
    public void tesSupremeIsWild() {
        final PaiGowHandRanking comparator = PaiGowHandRanking.SUPREME_SIX_HIGH;
        assertThat(comparator.comparePairs(
                new PaiGowPair(Domino.ofList(66, 42)),
                new PaiGowPair(Domino.ofList(21, 33))
        )).isEqualTo(-1);
    }

    @Test
    public void testFullNormalization() {
        final Hand hand = PaiGowHandRanking.SUPREME_BOTH_LOW.fullyNormalize(
                new Hand(
                        new PaiGowPair(Domino.ofList(66, 21)),
                        new PaiGowPair(Domino.ofList(42, 33))
                )
        );

        assertThat(hand).isEqualTo(
                new Hand(
                        new PaiGowPair(Domino.ofList(33, 42)),
                        new PaiGowPair(Domino.ofList(66, 21))
                )
        );
    }

    @Test
    public void testSupremeLowHigh() {
        //The extremely rare situations when Supremes can be high - and this is only possible with fives
        assertThat(PaiGowHandRanking.SUPREME_BOTH_LOW.comparePairs(new PaiGowPair(Domino.ofList(21, 41)), new PaiGowPair(Domino.ofList(42,32)))).isEqualTo(0);
        assertThat(PaiGowHandRanking.SUPREME_BOTH_HIGH.comparePairs(new PaiGowPair(Domino.ofList(21, 41)), new PaiGowPair(Domino.ofList(42,32)))).isEqualTo(0);
        assertThat(PaiGowHandRanking.SUPREME_SIX_HIGH.comparePairs(new PaiGowPair(Domino.ofList(21, 41)), new PaiGowPair(Domino.ofList(42,32)))).isEqualTo(-1);
    }
}
