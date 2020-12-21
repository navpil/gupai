package io.navpil.github.zenzen.jielong.player;

import io.navpil.github.zenzen.dominos.Domino;
import io.navpil.github.zenzen.jielong.Move;
import io.navpil.github.zenzen.jielong.SuanZhangAwareness;

import java.util.ArrayList;
import java.util.List;

public class RarenessPlayerImpl extends AbstractPlayerImpl {

    public RarenessPlayerImpl(String name, List<Domino> dominos) {
        super(name, dominos);
    }

    public RarenessPlayerImpl(String name, List<Domino> dominos, SuanZhangAwareness suanZhangAwareness) {
        super(name, dominos, suanZhangAwareness);
    }

    @Override
    protected void prioritizePutDown(ArrayList<Domino> dominosToPut) {
        dominosToPut.sort(getCommonFirst());
    }

    @Override
    protected void prioritizeLead(ArrayList<Domino> firstMoveList) {
        firstMoveList.sort(getCommonFirst());
    }

    @Override
    protected void prioritizeMove(List<Move> moves) {
        final RarityDominoSorter commonFirst = getCommonFirst();
        moves.sort((m1, m2) -> commonFirst.compare(m1.getDomino(), m2.getDomino()));
    }

    private RarityDominoSorter getCommonFirst() {
        return new RarityDominoSorter(getDominos());
    }
}
