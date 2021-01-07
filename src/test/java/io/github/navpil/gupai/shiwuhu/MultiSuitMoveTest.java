package io.github.navpil.gupai.shiwuhu;

import io.github.navpil.gupai.dominos.Domino;
import org.assertj.core.api.Assertions;
import org.junit.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

public class MultiSuitMoveTest {

    @Test
    public void testSameCompositionMultisuitMove() {
        final MultiSuitMove larger = new MultiSuitMove(Domino.ofList(11, 53, 53));
        final MultiSuitMove smaller = new MultiSuitMove(Domino.ofList(13, 32, 32));

        assertThat(smaller.beats(larger)).describedAs("Smaller should not beat larger").isFalse();
        assertThat(larger.beats(smaller)).describedAs("Larger should beat smaller").isTrue();
    }

    @Test
    public void testDifferentCompositionMultisuitMoveCannotBetEachOther() {
        final MultiSuitMove larger = new MultiSuitMove(Domino.ofList(11, 53, 53));
        final MultiSuitMove smaller = new MultiSuitMove(Domino.ofList(13, 41, 41));

        assertThat(smaller.beats(larger)).isFalse();
        assertThat(larger.beats(smaller)).isFalse();
    }

    @Test
    public void testLargerMultisuitMoveExtractsCorrectMove() {

        final MultiSuitMove lead = new MultiSuitMove(Domino.ofList(13, 32, 32));
        final MultiSuitMove my = new MultiSuitMove(Domino.ofList(11, 11, 53, 53, 53));

        assertThat(my.beats(lead)).isTrue();

        final List<Domino> dominos = my.extractBeat(lead);
        assertThat(new MultiSuitMove(dominos)).isEqualTo(new MultiSuitMove(Domino.ofList(11, 53, 53)));
    }

}