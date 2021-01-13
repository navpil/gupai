package io.github.navpil.gupai.fishing.tsungshap.fourtiles;

import io.github.navpil.gupai.dominos.Domino;
import io.github.navpil.gupai.fishing.tsungshap.NamedPlayer;
import io.github.navpil.gupai.fishing.tsungshap.TsungShapMove;
import io.github.navpil.gupai.fishing.tsungshap.TsungShapTable;

import java.util.LinkedList;
import java.util.List;

public interface Player extends NamedPlayer {

    void showTable(TsungShapTable table);

    void deal(List<Domino> initialDeal);

    Domino chooseSingleDiscard();

    boolean hasTiles();

    TsungShapMove chooseMove(LinkedList<Domino> row);

    void replenish(Domino replenishment);
}
