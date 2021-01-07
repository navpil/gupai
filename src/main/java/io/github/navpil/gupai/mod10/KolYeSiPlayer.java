package io.github.navpil.gupai.mod10;

import io.github.navpil.gupai.dominos.Domino;

public interface KolYeSiPlayer {

    int stake(Domino domino, int minStake, int maxStake);

    int dominoCount(Domino d1, Domino d2);

    void lose(int stake);

    void win(int stake);

    default boolean stillHasMoney() {
        return getMoney() > 0;
    }

    default boolean isBankrupt() {
        return !stillHasMoney();
    }

    int getMoney();

    String getName();
}
