package io.github.navpil.gupai.mod10.kolyesi;

import io.github.navpil.gupai.Domino;

public class KolYeSiBanker extends PlayerSkeletal {

    public KolYeSiBanker() {
        super("Banker", 0);
    }

    @Override
    public int stake(Domino domino, int minStake, int maxStake) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int dominoCount() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean stillHasMoney() {
        return true;
    }

    @Override
    public boolean isBankrupt() {
        return false;
    }
}
