package io.navpil.github.zenzen.jielong;

import io.navpil.github.zenzen.dominos.Domino;
import io.navpil.github.zenzen.jielong.player.RarityDominoSorter;
import org.assertj.core.api.Assertions;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class RarityDominoSorterTest {

    @Test
    public void testMostCommonGoFirst() {
        final ArrayList<Domino> dominos = new ArrayList<>(List.of(new Domino(1, 2), new Domino(1, 2), new Domino(1, 1), new Domino(3, 4)));

        dominos.sort(new RarityDominoSorter(dominos));

        Assertions.assertThat(dominos.get(0)).isEqualTo(new Domino(1, 1));
        Assertions.assertThat(dominos.get(3)).isEqualTo(new Domino(3, 4));
    }

}