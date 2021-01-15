package io.github.navpil.gupai.solution;

import io.github.navpil.gupai.BoardState;
import io.github.navpil.gupai.Move;
import io.github.navpil.gupai.Triplet;

import java.util.List;
import java.util.Set;

/**
 * Represents a solution with moves and initial triplets
 */
public class Solution {

    private final List<Move> moves;
    private final Set<BoardState> states;
    private final List<Triplet> triplets;
    private final int maxNoValueAdded;

    public Solution(List<Move> moves, Set<BoardState> states, List<Triplet> triplets, int maxNoValueAdded) {
        this.moves = moves;
        this.states = states;
        this.triplets = triplets;
        this.maxNoValueAdded = maxNoValueAdded;
    }

    public List<Move> getMoves() {
        return moves;
    }

    public Set<BoardState> getStates() {
        return states;
    }

    public List<Triplet> getTriplets() {
        return triplets;
    }

    public int getMaxNoValueAdded() {
        return maxNoValueAdded;
    }
}
