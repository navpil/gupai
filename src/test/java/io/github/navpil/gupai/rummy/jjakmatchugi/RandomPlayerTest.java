package io.github.navpil.gupai.rummy.jjakmatchugi;

import io.github.navpil.gupai.dominos.Domino;
import org.junit.Test;

import java.util.Collection;

import static org.assertj.core.api.Assertions.*;

public class RandomPlayerTest {

    @Test
    public void offer() {
        final RandomPlayer comp1 = new RandomPlayer("Comp1");
        comp1.deal(Domino.ofList(66,66,55,55,44));
        comp1.showTable(new Table(new RuleSet(RuleSet.Pairs.CHINESE, true, true)));

        final Collection<Domino> offer = comp1.offer(Domino.of(44));
        assertThat(offer).containsExactlyInAnyOrder(Domino.ofList(66,66,55,55,44,44).toArray(new Domino[0]));
        assertThat(comp1.won()).isTrue();
    }
}