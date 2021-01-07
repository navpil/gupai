package io.github.navpil.gupai.util;

import io.github.navpil.gupai.dominos.Domino;
import org.assertj.core.api.Assertions;
import org.junit.Test;

import java.util.List;

public class AbstractBagTest {

    @Test
    public void strictRemoveAll() {
        final HashBag<Domino> dominos = new HashBag<>(
                List.of(
                        Domino.of(6,2),
                        Domino.of(6,2),
                        Domino.of(5,4),
                        Domino.of(5,4),
                        Domino.of(4,3),
                        Domino.of(4,3),
                        Domino.of(2,1)
                )
        );

        dominos.strictRemoveAll(
                List.of(
                        Domino.of(6,2),
                        Domino.of(6,2),
                        Domino.of(6,2),
                        Domino.of(5,4),
                        Domino.of(5,4),
                        Domino.of(4,3),
                        Domino.of(1,1)
                )

        );

        Assertions.assertThat(dominos).isEqualTo(
                new HashBag<>(
                        List.of(
                                Domino.of(4,3),
                                Domino.of(2,1)
                        )
                )
        );


    }

    @Test
    public void strictRetainAll() {
        final HashBag<Domino> dominos = new HashBag<>(
                List.of(
                        Domino.of(6,2),
                        Domino.of(6,2),
                        Domino.of(5,4),
                        Domino.of(5,4),
                        Domino.of(4,3),
                        Domino.of(4,3),
                        Domino.of(2,1)
                )
        );

        dominos.strictRetainAll(
                List.of(
                        Domino.of(6,2),
                        Domino.of(6,2),
                        Domino.of(6,2),
                        Domino.of(5,4),
                        Domino.of(5,4),
                        Domino.of(4,3),
                        Domino.of(1,1)
                )

        );

        Assertions.assertThat(dominos).isEqualTo(
                new HashBag<>(
                        List.of(
                                Domino.of(6,2),
                                Domino.of(6,2),
                                Domino.of(5,4),
                                Domino.of(5,4),
                                Domino.of(4,3)
                        )
                )
        );

        Assertions.assertThat(dominos.size()).isEqualTo(5);

    }
}