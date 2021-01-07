package io.github.navpil.gupai.jielong.player.evaluators;

import io.github.navpil.gupai.jielong.Move;
import io.github.navpil.gupai.jielong.player.PlayerState;
import io.github.navpil.gupai.jielong.TableVisibleInformation;

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
