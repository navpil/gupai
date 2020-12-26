package io.navpil.github.zenzen.mod10;

import io.navpil.github.zenzen.dominos.Domino;

public class KolYeSiBanker implements KolYeSiPlayer {

    private int money;

    @Override
    public int stake(Domino domino, int minStake, int maxStake) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int dominoCount(Domino d1, Domino d2) {
        throw new UnsupportedOperationException();
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
    public boolean stillHasMoney() {
        return true;
    }

    @Override
    public boolean isBankrupt() {
        return false;
    }

    @Override
    public String getName() {
        return "Banker";
    }
}
