package io.github.navpil.gupai.xiangshifu;

import io.github.navpil.gupai.IDomino;
import io.github.navpil.gupai.util.Bag;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;

/**
 * Used to keep track of which boards were already analyzed
 */
public class BoardState {

    private final HashSet<Bag<IDomino>> board;

    public BoardState(List<Triplet> triplets) {
        board = new HashSet<>();
        for (Triplet triplet : triplets) {
            board.add(triplet.asBag());
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
