package io.github.navpil.gupai.mod10.kolyesi;

import io.github.navpil.gupai.Domino;

public interface KolYeSiPlayer {

    /**
     * How much to stake, depending on domino dealt
     *
     * @param domino tile dealt
     * @param minStake min possible stake
     * @param maxStake max possible stake
     * @return stake
     */
    int stake(Domino domino, int minStake, int maxStake);

    /**
     * How many tiles to ask for
     *
     * @return number of tiles (1 or 2)
     */
    int dominoCount();

    //All the following functions should theoretically have the same implementation (except for banker)
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
