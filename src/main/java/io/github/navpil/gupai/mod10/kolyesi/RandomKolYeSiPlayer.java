package io.github.navpil.gupai.mod10.kolyesi;

import io.github.navpil.gupai.Domino;

import java.util.Random;

public class RandomKolYeSiPlayer extends PlayerSkeletal {

    private final Random random = new Random();

    public RandomKolYeSiPlayer(String name, int money) {
        super(name, money);
    }

    @Override
    public int stake(Domino domino, int minStake, int maxStake) {
        return random.nextInt(Math.min(maxStake, getMoney()) + 1 - minStake) + minStake;
    }

    @Override
    public int dominoCount() {
        return random.nextInt(2) + 1;
    }

}
