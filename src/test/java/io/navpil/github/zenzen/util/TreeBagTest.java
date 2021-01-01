package io.navpil.github.zenzen.util;

import io.navpil.github.zenzen.dominos.Domino;
import org.assertj.core.api.Assertions;
import org.junit.Test;

import java.util.Iterator;

public class TreeBagTest {

    @Test
    public void testIterationDoesNotThrow() {
        final Bag<Domino> dominos = new TreeBag<>();
        dominos.add(Domino.of(2, 2));
        dominos.add(Domino.of(2, 2));
        dominos.add(Domino.of(6, 5));

        for (Domino domino : dominos) {
            System.out.println(domino);
        }
    }

    @Test
    public void shouldRemoveAll() {
        final Bag<Domino> dominos = new TreeBag<>();
        dominos.add(Domino.of(2, 2));
        dominos.add(Domino.of(2, 2));
        dominos.add(Domino.of(2, 2));
        dominos.add(Domino.of(6, 5));

        final Iterator<Domino> iterator = dominos.iterator();

        while (iterator.hasNext()) {
            iterator.next();
            iterator.remove();
        }

        Assertions.assertThat(dominos).isEmpty();
    }

    @Test
    public void remove() {
        final Bag<Domino> dominos = new TreeBag<>();
        dominos.add(Domino.of(2, 2));
        dominos.add(Domino.of(2, 2));
        dominos.add(Domino.of(2, 2));
        dominos.add(Domino.of(6, 5));

        final Iterator<Domino> iterator = dominos.iterator();

        boolean passedFirst22 = false;
        while (iterator.hasNext()) {
            final Domino next = iterator.next();
            if(Domino.of(2,2).equals(next) && !passedFirst22) {
                passedFirst22 = true;
            } else
            iterator.remove();
        }

        Assertions.assertThat(dominos.size()).isEqualTo(1);
        Assertions.assertThat(dominos).containsOnly(Domino.of(2,2));

        System.out.println(dominos);
    }
}