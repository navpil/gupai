package io.github.navpil.gupai.jielong.player.evaluators;

import io.github.navpil.gupai.jielong.Move;
import io.github.navpil.gupai.jielong.player.PlayerState;
import io.github.navpil.gupai.Domino;
import io.github.navpil.gupai.jielong.TableVisibleInformation;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Always gives preference to the largest/smallest tile, depending on situation.
 */
public class MinMaxMoveEvaluator implements MoveEvaluator {

    @Override
    public List<Integer> evaluateMove(List<Move> moves, TableVisibleInformation tableVisibleInformation, PlayerState playerState) {
        return moves.stream().map(move -> - getTotal(move.getDomino())).collect(Collectors.toList());
    }

    @Override
    public List<Integer> evaluatePutDown(List<Move> moves, TableVisibleInformation tableVisibleInformation, PlayerState playerState) {
        return moves.stream().map(move -> getTotal(move.getDomino())).collect(Collectors.toList());
    }

    private Integer getTotal(Domino domino) {
        return domino.getPips()[0] + domino.getPips()[1];
    }
}
