package io.github.navpil.gupai.mod10.daling;

import io.github.navpil.gupai.Domino;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractPlayer implements Player {

    private final String name;
    protected int money;
    protected ArrayList<Domino> dominos;

    public AbstractPlayer(String name, int money) {
        this.name = name;
        this.money = money;
    }

    @Override
    public void deal(List<Domino> deal) {
        dominos = new ArrayList<>(deal);
    }

    @Override
    public void win(Integer stake) {
        money += stake;
    }

    @Override
    public int getMoney() {
        return money;
    }

    @Override
    public String getName() {
        return name;
    }
}
