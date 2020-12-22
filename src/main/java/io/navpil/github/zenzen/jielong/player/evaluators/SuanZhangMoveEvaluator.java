package io.navpil.github.zenzen.jielong.player.evaluators;

import io.navpil.github.zenzen.dominos.Domino;
import io.navpil.github.zenzen.jielong.Move;
import io.navpil.github.zenzen.jielong.SuanZhang;
import io.navpil.github.zenzen.jielong.TableVisibleInformation;
import io.navpil.github.zenzen.jielong.player.PlayerState;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class SuanZhangMoveEvaluator implements MoveEvaluator {

    private final double maximumAcceptedPoints;

    public SuanZhangMoveEvaluator() {
        this(7.0);
    }

    public SuanZhangMoveEvaluator(double maximumAcceptedPoints) {
        this.maximumAcceptedPoints = maximumAcceptedPoints;
    }

    @Override
    public List<Integer> evaluateMove(List<Move> moves, TableVisibleInformation tableVisibleInformation, PlayerState playerState) {
        final SuanZhang suanZhang = tableVisibleInformation.getDragon().suanZhang();
        final Optional<Move> any = moves.stream().filter(m -> !suanZhang.willSuanZhang(m)).findAny();
        final ArrayList<Integer> priorities = new ArrayList<>();
        if (any.isPresent()) {
            for (Move move : moves) {
                final boolean willSuanZhang = suanZhang.willSuanZhang(move);
                move.setSuanZhang(willSuanZhang);
                if (willSuanZhang){
                    if (shouldSuanZhang(playerState, move.getDomino())) {
                        priorities.add(-1);
                    } else {
                        priorities.add(1);
                    }
                } else {
                    if (shouldSuanZhang(playerState, move.getDomino())) {
                        priorities.add(1);
                    } else {
                        priorities.add(-1);
                    }
                }
            }
        }
        return priorities;
    }

    public boolean shouldSuanZhang(PlayerState playerState, Domino toMove) {
        final List<Domino> dominos = new ArrayList<>(playerState.getDominos());
        dominos.remove(toMove);
        final Collection<Domino> putDown = playerState.getPutDown();
        int total = 0;
        for (Domino domino : dominos) {
            final int[] pips = domino.getPips();
            total += (pips[0] + pips[1]);
        }
        for (Domino domino : putDown) {
            final int[] pips = domino.getPips();
            total += (pips[0] + pips[1]);
        }
        return total * 1.0 / dominos.size() < (maximumAcceptedPoints + dominos.size() * 0.7);
    }


    @Override
    public List<Integer> evaluatePutDown(List<Move> moves, TableVisibleInformation tableVisibleInformation, PlayerState playerState) {
        return moves.stream().map(m -> 0).collect(Collectors.toList());
    }

    @Override
    public List<Integer> evaluateLead(List<Move> moves, TableVisibleInformation tableVisibleInformation, PlayerState playerState) {
        return moves.stream().map(m -> 0).collect(Collectors.toList());
    }
}
