package io.github.navpil.gupai.jielong.player;

import io.github.navpil.gupai.Domino;
import io.github.navpil.gupai.jielong.Move;

import java.util.ArrayList;
import java.util.List;

/**
 * Util class to find all possible moves given the dragon open ends and available dominoes.
 *
 * Does not calculate the discard moves - only the real moves.
 */
public class MoveFinder {

    public static List<Move> getAvailableMoves(List<Integer> openEnds, List<Domino> dominos) {
        List<Move> moves = new ArrayList<>();
        int openEndIndex = 0;
        for (Integer openEnd : openEnds) {
            openEndIndex++;
            for (Domino domino : dominos) {
                Move move = canConnect(openEndIndex, domino, openEnd);
                if (move != null) {
                    moves.add(move);
                }
            }
        }
        return moves;
    }

    private static Move canConnect(int openEndIndex, Domino domino, Integer openEnd) {
        final int[] pips = domino.getPips();
        if (pips[0] == openEnd) {
            return new Move(openEndIndex, pips[0], pips[1]);
        } else if (pips[1] == openEnd) {
            return new Move(openEndIndex, pips[1], pips[0]);
        }
        return null;
    }


}
