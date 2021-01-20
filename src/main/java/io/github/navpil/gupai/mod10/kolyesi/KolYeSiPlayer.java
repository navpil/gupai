package io.github.navpil.gupai.mod10.kolyesi;

import io.github.navpil.gupai.Domino;

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
