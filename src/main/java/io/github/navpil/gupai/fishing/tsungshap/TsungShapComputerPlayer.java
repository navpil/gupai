package io.github.navpil.gupai.fishing.tsungshap;

import io.github.navpil.gupai.dominos.Domino;
import io.github.navpil.gupai.dominos.DominoUtil;

import java.util.LinkedList;

public class TsungShapComputerPlayer implements TsungShapPlayer {

    private final String name;
    private HandHelper handHelper;

    public TsungShapComputerPlayer(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public TsungShapMove chooseMove(Domino domino, LinkedList<Domino> row) {
        TsungShapMove move = handHelper.fetchMaxMove(domino, row);
        if (move == null) {
            //This is a pretty arbitrary thing
            return new TsungShapMove(TsungShapMove.Type.DISCARD, DominoUtil.getPipPoints(domino) > 7 ? TsungShapMove.Side.LEFT : TsungShapMove.Side.RIGHT, domino);
        }
        return move;
    }

    @Override
    public void showTable(TsungShapTable table) {
        handHelper = new HandHelper(table.getRuleSet());
    }
}
