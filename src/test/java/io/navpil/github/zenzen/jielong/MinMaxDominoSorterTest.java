package io.navpil.github.zenzen.jielong;

import io.navpil.github.zenzen.dominos.Domino;
import io.navpil.github.zenzen.jielong.player.MinMaxDominoSorter;
import org.assertj.core.api.Assertions;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class MinMaxDominoSorterTest {

    @Test
    public void testMinMax() {
        final ArrayList<Domino> dominos = new ArrayList<>(List.of(new Domino(2, 1), new Domino(3, 5), new Domino(1, 1)));

        dominos.sort(MinMaxDominoSorter.SMALL_FIRST);

        Assertions.assertThat(dominos.get(0)).isEqualTo(new Domino(1,1));
        Assertions.assertThat(dominos.get(2)).isEqualTo(new Domino(3, 5));
    }

}