package io.navpil.github.zenzen.fishing.tsungshap;

import io.navpil.github.zenzen.dominos.Domino;

import java.util.LinkedList;

public interface TsungShapPlayer extends NamedPlayer {

    String getName();

    TsungShapMove chooseMove(Domino domino, LinkedList<Domino> row);

    void showTable(TsungShapTable table);

}
