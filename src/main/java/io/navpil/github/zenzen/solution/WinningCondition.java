package io.navpil.github.zenzen.solution;

import io.navpil.github.zenzen.Move;
import io.navpil.github.zenzen.Triplet;

import java.util.List;

public interface WinningCondition {

    boolean hasWon(List<Triplet> triplets);

    int compareMoves(Move m1, Move m2);

}
