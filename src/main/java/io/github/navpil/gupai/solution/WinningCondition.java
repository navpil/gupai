package io.github.navpil.gupai.solution;

import io.github.navpil.gupai.Move;
import io.github.navpil.gupai.Triplet;

import java.util.List;

/**
 * Winning condition also contains how to compare moves to come to the solution faster
 */
public interface WinningCondition {

    boolean hasWon(List<Triplet> triplets);

    int compareMoves(Move m1, Move m2);

}
