package io.github.navpil.gupai;

import io.github.navpil.gupai.xiangshifu.Triplet;
import org.junit.Test;

import java.util.Collection;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

public class ChineseDominoSetTest {

    @Test
    public void test10Sets() {
        final List<Triplet> triplets = ChineseDominoSet.random10Triplets();
        assertThat(triplets.size()).isEqualTo(11);

        assertThat(triplets.stream().map(Triplet::asBag).flatMap(Collection::stream).filter(d -> d instanceof Domino).count()).isEqualTo(32);
    }

    @Test
    public void test8Sets() {
        final List<Triplet> triplets = ChineseDominoSet.random8Triplets();
        assertThat(triplets.size()).isEqualTo(8 + 4);
        assertThat(triplets.stream().map(Triplet::asBag).flatMap(Collection::stream).filter(d -> d instanceof Domino).count()).isEqualTo(32);
    }

}
