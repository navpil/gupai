package io.github.navpil.gupai.mod10.kolyesi;

import io.github.navpil.gupai.Domino;

public class FixedKolYeSiPlayer implements KolYeSiPlayer {

    private final String name;
    private final int stake;
    private final int count;
    private int money;

    public FixedKolYeSiPlayer(String name, int money, int stake, int count) {
        this.name = name;
        this.money = money;
        this.stake = stake;
        this.count = count;

    }

    @Override
    public int stake(Domino domino, int minStake, int maxStake) {
        return Math.max(minStake, Math.min(Math.min(maxStake, this.money), stake));
    }

    @Override
    public int dominoCount(Domino d1, Domino d2) {
        return count;
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
