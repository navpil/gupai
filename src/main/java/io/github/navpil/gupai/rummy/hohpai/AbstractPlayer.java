package io.github.navpil.gupai.rummy.hohpai;

import io.github.navpil.gupai.Domino;

import java.util.ArrayList;
import java.util.Collection;

public abstract class AbstractPlayer implements Player {

    private final String name;
    protected Table table;
    protected ArrayList<Domino> dominos;
    protected HandCalculator handCalculator;

    public AbstractPlayer(String name) {
        this.name = name;
    }


    protected Collection<Hand> getHands() {
        return handCalculator
                .handPermutations(dominos);
    }

    @Override
    public void deal(Collection<Domino> deal) {
        dominos = new ArrayList<>(deal);
    }

    @Override
    public void showTable(Table table) {
        this.table = table;
        handCalculator = new HandCalculator(table.getRuleSet());
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "AbstractPlayer{" +
                "name='" + name + '\'' +
                ", dominos=" + dominos +
                '}';
    }
}
