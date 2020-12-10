package io.navpil.github.zenzen.jielong.player;

import io.navpil.github.zenzen.dominos.Domino;
import io.navpil.github.zenzen.jielong.Move;

import java.util.ArrayList;
import java.util.List;

public class MinMaxPlayerImpl extends AbstractPlayerImpl {

    public MinMaxPlayerImpl(String name, List<Domino> dominos) {
        super(name, dominos);
    }

    @Override
    protected void prioritizePutDown(ArrayList<Domino> dominosToPut) {
        dominosToPut.sort(MinMaxDominoSorter.SMALL_FIRST);
    }

    @Override
    protected void prioritizeLead(ArrayList<Domino> firstMoveList) {
        firstMoveList.sort(MinMaxDominoSorter.BIG_FIRST);
    }

    @Override
    protected void prioritizeMove(List<Move> moves) {
        moves.sort((m1, m2) -> MinMaxDominoSorter.BIG_FIRST.compare(m1.getDomino(), m2.getDomino()));
    }
}
