package io.navpil.github.zenzen.jielong.player;

import io.navpil.github.zenzen.dominos.Domino;
import io.navpil.github.zenzen.jielong.Dragon;
import io.navpil.github.zenzen.jielong.Move;
import io.navpil.github.zenzen.jielong.TableVisibleInformation;
import io.navpil.github.zenzen.jielong.player.evaluators.MoveEvaluator;
import io.navpil.github.zenzen.jielong.player.evaluators.PriorityUtil;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class PriorityPlayer implements Player {

    private final String name;
    private final List<Domino> dominos;
    private final Collection<Domino> putDown = new ArrayList<>();
    private final MoveEvaluator moveEvaluator;

    public PriorityPlayer(String name, List<Domino> dominos, MoveEvaluator moveEvaluator) {
        this.name = name;
        this.dominos = new ArrayList<>(dominos);
        this.moveEvaluator = moveEvaluator;
    }

    @Override
    public Move firstMove() {
        final List<Move> moves = dominos.stream().map(d -> new Move(0, d.getPips()[0], d.getPips()[1])).collect(Collectors.toList());

        final TableVisibleInformation irrelevant = null;
        final List<Integer> priorities = moveEvaluator.evaluateLead(moves, irrelevant, new PlayerState(dominos, putDown));

        final List<Move> sorted = PriorityUtil.sort(priorities, moves);
        final Move move = sorted.get(0);

        dominos.remove(move.getDomino());
        return move;
    }

    @Override
    public Move extractMove(Dragon dragon) {
        List<Integer> openEnds = dragon.getOpenEnds();

        List<Move> moves = MoveFinder.getAvailableMoves(openEnds, dominos);

        if (!moves.isEmpty()) {
            final List<Integer> priorities = moveEvaluator.evaluateMove(moves, new TableVisibleInformation(dragon), new PlayerState(dominos, putDown));
            final List<Move> sorted = PriorityUtil.sort(priorities, moves);
            final Move move = sorted.get(0);
            dominos.remove(move.getDomino());
            return move;
        }

        final List<Move> putDownMoves = dominos.stream().map(Move::none).collect(Collectors.toList());
        final List<Integer> priorities = moveEvaluator.evaluatePutDown(putDownMoves, new TableVisibleInformation(dragon), new PlayerState(dominos, putDown));

        final List<Move> sorted = PriorityUtil.sort(priorities, putDownMoves);
        final Move move = sorted.get(0);

        dominos.remove(move.getDomino());
        putDown.add(move.getDomino());
        return move;
    }

    @Override
    public int getPoints() {
        int score = 0;
        for (Domino domino : dominos) {
            score += (domino.getPips()[0] + domino.getPips()[1]);
        }
        for (Domino domino : putDown) {
            score += (domino.getPips()[0] + domino.getPips()[1]);
        }
        return score;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "AbstractPlayerImpl{" +
                "name='" + name + '\'' +
                ", dominos=" + dominos +
                ", putDown=" + putDown +
                '}';
    }
}
