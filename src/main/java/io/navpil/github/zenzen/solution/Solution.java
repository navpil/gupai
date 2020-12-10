package io.navpil.github.zenzen.solution;

import io.navpil.github.zenzen.BoardState;
import io.navpil.github.zenzen.Move;
import io.navpil.github.zenzen.Triplet;

import java.util.List;
import java.util.Set;

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
