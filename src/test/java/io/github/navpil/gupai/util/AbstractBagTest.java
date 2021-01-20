package io.github.navpil.gupai.util;

import io.github.navpil.gupai.Domino;
import org.junit.Test;

import java.util.Iterator;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

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

        assertThat(dominos).isEqualTo(
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

        assertThat(dominos).isEqualTo(
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

        assertThat(dominos.size()).isEqualTo(5);

    }

    @Test
    public void sizeTest() {
        final HashBag<String> bag = HashBag.of("A", "A", "B", "B", "C", "C");
        assertThat(bag.size()).isEqualTo(6);

        bag.removeAll(List.of("A"));
        assertThat(bag.size()).isEqualTo(4);

        bag.strictRemoveAll(List.of("B", "C"));
        assertThat(bag.size()).isEqualTo(2);

        bag.addAll(List.of("D", "D"));
        assertThat(bag.size()).isEqualTo(4);

        bag.strictRetainAll(List.of("C", "D", "E"));
        assertThat(bag.size()).isEqualTo(2);

        bag.add("D");
        assertThat(bag.size()).isEqualTo(3);

        bag.retainAll(List.of("D", "E"));
        assertThat(bag.size()).isEqualTo(2);

        final Iterator<String> it = bag.iterator();
        it.next();
        it.remove();

        assertThat(bag.size()).isEqualTo(1);

        it.remove();
        assertThat(bag.size()).isEqualTo(0);
        assertThat(bag.isEmpty()).isTrue();

        bag.remove("G");
        assertThat(bag.size()).isEqualTo(0);
        assertThat(bag.isEmpty()).isTrue();

    }
}
