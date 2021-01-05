package io.navpil.github.zenzen.jielong.player.evaluators;

import io.navpil.github.zenzen.jielong.Move;
import io.navpil.github.zenzen.jielong.TableVisibleInformation;
import io.navpil.github.zenzen.jielong.player.PlayerState;

import java.util.List;

/**
 * Describes a logic to choose the best tile for move/lead/discard.
 *
 * Stateless
 */
public interface MoveEvaluator {

    List<Integer> evaluateMove(List<Move> moves, TableVisibleInformation tableVisibleInformation, PlayerState playerState);

    default List<Integer> evaluateLead(List<Move> moves, TableVisibleInformation tableVisibleInformation, PlayerState playerState) {
        return evaluateMove(moves, tableVisibleInformation, playerState);
    }

    List<Integer> evaluatePutDown(List<Move> moves, TableVisibleInformation tableVisibleInformation, PlayerState playerState);
}
