package io.github.navpil.gupai.mod10.paigow;

import io.github.navpil.gupai.dominos.Domino;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractPlayer implements Player {

    private final String name;
    private int money;
    protected ArrayList<Domino> dominos;

    public AbstractPlayer(String name, int money) {
        this.name = name;
        this.money = money;
    }

    @Override
    public int stake(int maxStake) {
        return getStakeAmount(Math.min(maxStake, money));
    }

    protected int getStakeAmount(int max) {
        return Math.min(5, max);
    }

    @Override
    public void deal(List<Domino> take) {
        dominos = new ArrayList<>(take);
    }

    @Override
    public void win(int stake) {
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
