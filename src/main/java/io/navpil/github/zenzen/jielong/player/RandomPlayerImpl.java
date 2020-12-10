package io.navpil.github.zenzen.jielong.player;

import io.navpil.github.zenzen.dominos.Domino;
import io.navpil.github.zenzen.jielong.Move;

import java.util.ArrayList;
import java.util.List;

public class RandomPlayerImpl extends AbstractPlayerImpl {

    public RandomPlayerImpl(String name, List<Domino> dominos) {
        super(name, dominos);
    }

    @Override
    protected void prioritizePutDown(ArrayList<Domino> dominosToPut) {
        //do nothing
    }

    @Override
    protected void prioritizeLead(ArrayList<Domino> firstMoveList) {
        //do nothing
    }

    @Override
    protected void prioritizeMove(List<Move> moves) {
        //do nothing
    }
}
