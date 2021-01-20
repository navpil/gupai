package io.github.navpil.gupai.mod10.taungau;

import io.github.navpil.gupai.Domino;
import io.github.navpil.gupai.util.HashBag;

import java.util.Collection;

public abstract class AbstractPlayer implements Player {

    private final String name;
    private int money;
    protected HashBag<Domino> dominos;

    protected AbstractPlayer(String name, int money) {
        this.name = name;
        this.money = money;
    }

    @Override
    public void deal(Collection<Domino> dominos) {
        this.dominos = new HashBag<>(dominos);
    }

    @Override
    public Collection<Domino> hand() {
        return dominos;
    }

    @Override
    public void lose(int stake) {
        money -= stake;
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
