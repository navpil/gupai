package io.navpil.github.zenzen.jielong.player.evaluators;

import io.navpil.github.zenzen.dominos.Domino;
import io.navpil.github.zenzen.jielong.Move;
import io.navpil.github.zenzen.jielong.TableVisibleInformation;
import io.navpil.github.zenzen.jielong.player.PlayerState;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Tries to keep the rarest tile in the hand
 */
public class RarenessMoveEvaluator implements MoveEvaluator {

    @Override
    public List<Integer> evaluateMove(List<Move> moves, TableVisibleInformation tableVisibleInformation, PlayerState playerState) {
        final List<Domino> dominos = playerState.getDominos();
        final int[] occurences = occurences(dominos);
        return moves.stream().map(m -> - index(m.getDomino(), occurences)).collect(Collectors.toList());
    }

    @Override
    public List<Integer> evaluatePutDown(List<Move> moves, TableVisibleInformation tableVisibleInformation, PlayerState playerState) {
        return evaluateMove(moves, tableVisibleInformation, playerState);
    }

    private int[] occurences(Collection<Domino> dominos) {
        int[] occurrences = new int[]{0, 0, 0, 0, 0, 0, 0};
        for (Domino domino : dominos) {
            final int[] pips = domino.getPips();
            occurrences[pips[0]]++;
            occurrences[pips[1]]++;
        }
        return occurrences;
    }

    private int index(Domino domino, int [] occurrences) {
        final int[] pips = domino.getPips();
        return occurrences[pips[0]] + occurrences[pips[1]];
    }

}
