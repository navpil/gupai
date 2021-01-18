package io.github.navpil.gupai.rummy.hohpai;

import io.github.navpil.gupai.fishing.tsungshap.NamedPlayer;
import io.github.navpil.gupai.dominos.Domino;

import java.util.Collection;
import java.util.Collections;

public interface Player extends NamedPlayer {

    void deal(Collection<Domino> deal);

    void give(Domino give);

    default boolean won() {
        return getWinningHand() != null;
    }

    Hand getWinningHand();

    Domino discard();

    void showTable(Table table);

    //By default we ignore the last discard
    default Collection<Domino> offer(Domino lastDiscard) {
        return Collections.emptyList();
    }
}
