package io.github.navpil.gupai;

import io.github.navpil.gupai.dominos.IDomino;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;

public class BoardState {

    private final HashSet<List<IDomino>> board;

    public BoardState(List<Triplet> triplets) {
        board = new HashSet<>();
        for (Triplet triplet : triplets) {
            board.add(triplet.asList());
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BoardState that = (BoardState) o;
        return Objects.equals(board, that.board);
    }

    @Override
    public int hashCode() {
        return Objects.hash(board);
    }
}
