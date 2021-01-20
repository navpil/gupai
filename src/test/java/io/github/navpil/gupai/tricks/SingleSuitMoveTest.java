package io.github.navpil.gupai.tricks;

import io.github.navpil.gupai.Domino;
import io.github.navpil.gupai.tricks.shiwuhu.SingleSuitMove;
import org.junit.Test;

import java.util.List;

public class SingleSuitMoveTest {

    @Test
    public void test() {
        System.out.println(new SingleSuitMove(List.of(new Domino(4,4), new Domino(4,4))).beats(new SingleSuitMove(List.of(new Domino(3,1)))));
    }

}
