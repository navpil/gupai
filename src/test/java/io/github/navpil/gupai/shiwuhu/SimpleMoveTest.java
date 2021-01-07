package io.github.navpil.gupai.shiwuhu;

import io.github.navpil.gupai.dominos.Domino;
import org.junit.Test;

import java.util.List;

public class SimpleMoveTest {

    @Test
    public void test() {
        System.out.println(new SimpleMove(List.of(new Domino(4,4), new Domino(4,4))).beats(new SimpleMove(List.of(new Domino(3,1)))));
    }

}