package io.github.navpil.gupai;

import io.github.navpil.gupai.dominos.Domino;
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