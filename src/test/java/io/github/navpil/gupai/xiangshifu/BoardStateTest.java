package io.github.navpil.gupai.xiangshifu;

import io.github.navpil.gupai.Domino;
import io.github.navpil.gupai.xiangshifu.BoardState;
import io.github.navpil.gupai.xiangshifu.Triplet;
import org.junit.Test;

import java.util.HashSet;
import java.util.List;

public class BoardStateTest {

    @Test
    public void test() {
        final BoardState boardState = new BoardState(List.of(new Triplet(List.of(new Domino(1, 2), new Domino(2, 3)))));

        final HashSet<BoardState> boardStates = new HashSet<>();
        assert boardStates.add(boardState);
        assert !boardStates.add(boardState);
    }

}
