package io.github.navpil.gupai.xiangshifu.solution;

import io.github.navpil.gupai.xiangshifu.Move;
import io.github.navpil.gupai.xiangshifu.Triplet;

import java.util.List;

/**
 * Winning condition also contains how to compare moves to come to the solution faster
 */
public interface WinningCondition {

    boolean hasWon(List<Triplet> triplets);

    int compareMoves(Move m1, Move m2);

}
