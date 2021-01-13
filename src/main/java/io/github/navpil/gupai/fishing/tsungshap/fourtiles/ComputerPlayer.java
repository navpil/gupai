package io.github.navpil.gupai.fishing.tsungshap.fourtiles;

import io.github.navpil.gupai.dominos.Domino;
import io.github.navpil.gupai.dominos.DominoUtil;
import io.github.navpil.gupai.fishing.tsungshap.TsungShapMove;

import java.util.LinkedList;

public class ComputerPlayer extends AbstractPlayer {

    public ComputerPlayer(String name) {
        super(name);
    }

    @Override
    public Domino chooseSingleDiscard() {
        return withDominoRemoved(selectBestDiscard());
    }

    private Domino selectBestDiscard() {
        //For now remove the last one. I
        return dominos.get(dominos.size() - 1);
    }

    @Override
    public TsungShapMove chooseMove(LinkedList<Domino> row) {
        TsungShapMove move = handHelper.fetchMaxMove(dominos, row);
        if (move == null) {
            final Domino domino = selectBestDiscard();
            final TsungShapMove discard = new TsungShapMove(TsungShapMove.Type.DISCARD, DominoUtil.getPipPoints(domino) > 7 ? TsungShapMove.Side.LEFT : TsungShapMove.Side.RIGHT, domino);
            return withDominoRemoved(discard);
        }
        return withDominoRemoved(move);
    }
}
