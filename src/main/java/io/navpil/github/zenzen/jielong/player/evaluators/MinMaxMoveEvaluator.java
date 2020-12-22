package io.navpil.github.zenzen.jielong.player.evaluators;

import io.navpil.github.zenzen.dominos.Domino;
import io.navpil.github.zenzen.jielong.Move;
import io.navpil.github.zenzen.jielong.TableVisibleInformation;
import io.navpil.github.zenzen.jielong.player.PlayerState;

import java.util.List;
import java.util.stream.Collectors;

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
