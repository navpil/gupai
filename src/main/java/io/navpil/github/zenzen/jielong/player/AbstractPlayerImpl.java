package io.navpil.github.zenzen.jielong.player;

import io.navpil.github.zenzen.dominos.Domino;
import io.navpil.github.zenzen.jielong.Dragon;
import io.navpil.github.zenzen.jielong.Move;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public abstract class AbstractPlayerImpl implements Player {

    private final String name;
    private final List<Domino> dominos;
    private final Collection<Domino> putDown = new ArrayList<>();

    public AbstractPlayerImpl(String name, List<Domino> dominos) {
        this.name = name;
        this.dominos = new ArrayList<>(dominos);
    }

    @Override
    public Move firstMove() {
        final ArrayList<Domino> firstMoveList = new ArrayList<>(dominos);
        prioritizeLead(firstMoveList);
        final Domino remove = firstMoveList.get(0);
        dominos.remove(remove);
        return new Move(0, remove.getPips()[0], remove.getPips()[1]);
    }

    @Override
    public Move extractMove(Dragon dragon) {
        List<Integer> openEnds = dragon.getOpenEnds();
        int openEndIndex = 0;

        List<Move> moves = new ArrayList<>();

        for (Integer openEnd : openEnds) {
            openEndIndex++;
            for (Domino domino : dominos) {
                Move move = canConnect(openEndIndex, domino, openEnd);
                if (move != null) {
                    moves.add(move);
                }
            }
        }
        prioritizeMove(moves);
        if (!moves.isEmpty()) {
            final Move move = moves.get(0);
            dominos.remove(move.getDomino());
            return move;
        }
        final ArrayList<Domino> dominosToPut = new ArrayList<>(dominos);
        prioritizePutDown(dominosToPut);
        final Domino dominoToPut = dominosToPut.get(0);
        dominos.remove(dominoToPut);
        putDown.add(dominoToPut);
        return Move.none(dominoToPut);
    }

    protected abstract void prioritizePutDown(ArrayList<Domino> dominosToPut);

    protected abstract void prioritizeLead(ArrayList<Domino> firstMoveList);

    protected abstract void prioritizeMove(List<Move> moves);

    protected List<Domino> getDominos() {
        return dominos;
    }

    private Move canConnect(int openEndIndex, Domino domino, Integer openEnd) {
        final int[] pips = domino.getPips();
        if (pips[0] == openEnd) {
            return new Move(openEndIndex, pips[0], pips[1]);
        } else if (pips[1] == openEnd) {
            return new Move(openEndIndex, pips[1], pips[0]);
        }
        return null;
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
