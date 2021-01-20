package io.github.navpil.gupai.mod10.kolyesi;

import io.github.navpil.gupai.Domino;

import java.util.Random;

public class RandomKolYeSiPlayer implements KolYeSiPlayer {

    private final String name;
    private int money;
    private final Random random = new Random();

    public RandomKolYeSiPlayer(String name, int money) {
        this.name = name;
        this.money = money;

    }

    @Override
    public int stake(Domino domino, int minStake, int maxStake) {
        return random.nextInt(Math.min(maxStake, this.money) + 1 - minStake) + minStake;
    }

    @Override
    public int dominoCount(Domino d1, Domino d2) {
        return random.nextInt(2) + 1;
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
