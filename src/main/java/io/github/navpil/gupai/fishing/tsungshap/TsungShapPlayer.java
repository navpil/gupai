package io.github.navpil.gupai.fishing.tsungshap;

import io.github.navpil.gupai.Domino;

import java.util.LinkedList;

public interface TsungShapPlayer extends NamedPlayer {

    String getName();

    TsungShapMove chooseMove(Domino domino, LinkedList<Domino> row);

    void showTable(TsungShapTable table);

}
