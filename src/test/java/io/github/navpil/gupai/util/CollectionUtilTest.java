package io.github.navpil.gupai.util;

import io.github.navpil.gupai.dominos.Domino;
import org.junit.Test;

import java.util.Collection;

import static org.assertj.core.api.Assertions.*;

public class CollectionUtilTest {

    @Test
    public void allPermutations() {

        final Collection<Domino> dominos = Domino.ofList(54, 43, 21, 21);
        final Collection<Collection<Domino>> collections = CollectionUtil.allPermutations(dominos, 3);

        System.out.println(collections);

        assertThat(collections.size()).isEqualTo(4);

    }
}