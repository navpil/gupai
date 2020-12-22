package io.navpil.github.zenzen.jielong.player.evaluators;

import io.navpil.github.zenzen.dominos.DominoUtil;
import io.navpil.github.zenzen.jielong.Move;
import io.navpil.github.zenzen.jielong.TableVisibleInformation;
import io.navpil.github.zenzen.jielong.player.Counter;
import io.navpil.github.zenzen.jielong.player.PlayerState;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

public class PipCounterMoveEvaluator implements MoveEvaluator {

    @Override
    public List<Integer> evaluateMove(List<Move> moves, TableVisibleInformation tableVisibleInformation, PlayerState playerState) {
        final Collection<Integer> openEnds = new HashSet<>(tableVisibleInformation.getDragon().getOpenEnds());

        final List<Counter> priorities = moves.stream().map(s -> new Counter()).collect(Collectors.toList());

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

        return priorities.stream().map(Counter::getCount).collect(Collectors.toList());
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
