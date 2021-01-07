package io.github.navpil.gupai.rummy.hohpai;

import io.github.navpil.gupai.fishing.tsungshap.NamedPlayer;
import io.github.navpil.gupai.dominos.Domino;

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
