package io.github.navpil.gupai.mod10.kolyesi;

import io.github.navpil.gupai.Domino;

/**
 * Player who always puts the same stake and takes the same amount of dominoes
 */
public class FixedKolYeSiPlayer extends PlayerSkeletal {

    //Fixed stake
    private final int stake;
    //Fixed number of dominoes taken (1 or 2)
    private final int count;

    public FixedKolYeSiPlayer(String name, int money, int stake, int count) {
        super(name, money);
        this.stake = stake;
        this.count = count;

    }

    @Override
    public int stake(Domino domino, int minStake, int maxStake) {
        return Math.max(minStake, Math.min(Math.min(maxStake,  getMoney()), stake));
    }

    @Override
    public int dominoCount() {
        return count;
    }

}
