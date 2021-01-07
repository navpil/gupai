package io.github.navpil.gupai.jielong.player.evaluators;

import io.github.navpil.gupai.jielong.player.MutableInteger;
import io.github.navpil.gupai.dominos.DominoUtil;
import io.github.navpil.gupai.jielong.Move;
import io.github.navpil.gupai.jielong.TableVisibleInformation;
import io.github.navpil.gupai.jielong.player.PlayerState;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Evaluator which tries to correctly decide on whether to close an end or to put a double.
 *
 * For example given the dragon of [4:6][6:5] player may play [6:4] by still having a [4:4] double in the hand.
 * This evaluator tries to prevent this kind of mistake.
 *
 * Another thing is that if all the tiles of certain number are already on the table or in own hand,
 * there is no point of putting the tile of this number unless there are no other moves.
 */
public class PipCounterMoveEvaluator implements MoveEvaluator {

    @Override
    public List<Integer> evaluateMove(List<Move> moves, TableVisibleInformation tableVisibleInformation, PlayerState playerState) {
        final Collection<Integer> openEnds = new HashSet<>(tableVisibleInformation.getDragon().getOpenEnds());

        final List<MutableInteger> priorities = moves.stream().map(s -> new MutableInteger()).collect(Collectors.toList());

        for (Integer openEnd : openEnds) {
            final int leftPipsOnAllHands = tableVisibleInformation.getDragon().getPipTracker().count(openEnd);
            final long ownPips = playerState.getDominos().stream().filter(d -> !DominoUtil.isDouble(d)).filter(d -> DominoUtil.containsPip(d, openEnd)).count();
            final long putDownPips = playerState.getPutDown().stream().filter(d -> !DominoUtil.isDouble(d)).filter(d -> DominoUtil.containsPip(d, openEnd)).count();

            final long pipsLeftEstimate = leftPipsOnAllHands - ownPips - putDownPips;

            if (pipsLeftEstimate == 0) {
                //No need to worry about these moves - only us have an access to the end.
                //Move ordinary move very far, and a double a bit closer
                for (int i = 0; i < moves.size(); i++) {
                    final Move move = moves.get(i);
                    if (move.getInwards() == openEnd) {
                        if (DominoUtil.isDouble(move.getDomino())) {
                            priorities.get(i).add(2);
                        } else {
                            priorities.get(i).add(1);
                        }
                    }
                }
            } else {
                //Have to put doubles immediately, otherwise we risk keeping the double in our hand
                int priority = pipsLeftEstimate == 1 ? -2 : -1;
                for (int i = 0; i < moves.size(); i++) {
                    final Move move = moves.get(i);
                    if (move.getInwards() == openEnd && DominoUtil.isDouble(move.getDomino())) {
                        priorities.get(i).add(priority);
                    }
                }
            }
        }

        return priorities.stream().map(MutableInteger::getCount).collect(Collectors.toList());
    }

    @Override
    public List<Integer> evaluatePutDown(List<Move> moves, TableVisibleInformation tableVisibleInformation, PlayerState playerState) {
        return moves.stream().map(i -> 0).collect(Collectors.toList());
    }

    @Override
    public List<Integer> evaluateLead(List<Move> moves, TableVisibleInformation tableVisibleInformation, PlayerState playerState) {
        return moves.stream().map(i -> 0).collect(Collectors.toList());
    }
}
