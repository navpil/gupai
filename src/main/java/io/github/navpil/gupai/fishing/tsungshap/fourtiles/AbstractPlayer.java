package io.github.navpil.gupai.fishing.tsungshap.fourtiles;

import io.github.navpil.gupai.Domino;
import io.github.navpil.gupai.fishing.tsungshap.HandHelper;
import io.github.navpil.gupai.fishing.tsungshap.TsungShapMove;
import io.github.navpil.gupai.fishing.tsungshap.TsungShapTable;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractPlayer implements Player {

    private final String name;
    protected TsungShapTable table;
    protected final List<Domino> dominos = new ArrayList<>();
    protected HandHelper handHelper;

    public AbstractPlayer(String name) {
        this.name = name;
    }

    @Override
    public void showTable(TsungShapTable table) {
        this.table = table;
        handHelper = new HandHelper(table.getRuleSet());
    }

    @Override
    public void deal(List<Domino> initialDeal) {
        dominos.clear();
        dominos.addAll(initialDeal);
    }

    @Override
    public boolean hasTiles() {
        return !dominos.isEmpty();
    }

    @Override
    public void replenish(Domino replenishment) {
        dominos.add(replenishment);
    }

    @Override
    public String getName() {
        return name;
    }

    protected TsungShapMove withDominoRemoved(TsungShapMove move) {
        dominos.remove(move.getDomino());
        return move;
    }

    protected Domino withDominoRemoved(Domino domino) {
        dominos.remove(domino);
        return domino;
    }
}
