package io.navpil.github.zenzen.mod10;

import io.navpil.github.zenzen.dominos.Domino;

public interface KolYeSiPlayer {

    int stake(Domino domino, int minStake, int maxStake);

    int dominoCount(Domino d1, Domino d2);

    void lose(int stake);

    void win(int stake);

    default boolean stillHasMoney() {
        return getMoney() > 0;
    }

    int getMoney();

    String getName();
}
