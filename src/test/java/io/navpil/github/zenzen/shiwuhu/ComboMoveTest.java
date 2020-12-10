package io.navpil.github.zenzen.shiwuhu;

import io.navpil.github.zenzen.dominos.Domino;
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