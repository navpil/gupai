package io.github.navpil.gupai.tricks.tianjiu;

import io.github.navpil.gupai.Domino;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public abstract class AbstractPlayer implements Player {

    private final String name;
    protected List<Domino> dominos;
    protected Table table;

    public AbstractPlayer(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void deal(Collection<Domino> deal) {
        this.dominos = new ArrayList<>(deal);
    }

    @Override
    public void showTable(Table table) {
        this.table = table;
    }
}
