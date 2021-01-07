package io.github.navpil.gupai.shiwuhu;

import io.github.navpil.gupai.dominos.Domino;
import org.junit.Test;

import java.util.List;

public class ComboMoveTest {

    @Test
    public void test() {
        final ComboMove lead = new ComboMove(List.of(new Domino(1, 1), new Domino(6, 2), new Domino(6, 2)));
        final ComboMove my = new ComboMove(List.of(new Domino(1, 3), new Domino(3, 2), new Domino(3, 2)));
        System.out.println(my.beats(lead));
    }

}