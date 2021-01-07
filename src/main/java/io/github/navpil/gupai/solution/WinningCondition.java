package io.github.navpil.gupai.solution;

import io.github.navpil.gupai.Move;
import io.github.navpil.gupai.Triplet;

import java.util.List;

public interface WinningCondition {

    boolean hasWon(List<Triplet> triplets);

    int compareMoves(Move m1, Move m2);

}
