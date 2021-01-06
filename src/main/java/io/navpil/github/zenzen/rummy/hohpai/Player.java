package io.navpil.github.zenzen.rummy.hohpai;

import io.navpil.github.zenzen.dominos.Domino;
import io.navpil.github.zenzen.fishing.tsungshap.NamedPlayer;

import java.util.Collection;

public interface Player extends NamedPlayer {

    void deal(Collection<Domino> deal);

    void give(Domino give);

    default boolean hasWon() {
        return getWinningHand() != null;
    }

    Hand getWinningHand();

    Domino getDiscard();

    void showTable(Table table);

}
