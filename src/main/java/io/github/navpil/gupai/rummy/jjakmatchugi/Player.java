package io.github.navpil.gupai.rummy.jjakmatchugi;

import io.github.navpil.gupai.Domino;
import io.github.navpil.gupai.fishing.tsungshap.NamedPlayer;

import java.util.Collection;
import java.util.List;

public interface Player extends NamedPlayer {

    default boolean won() {
        return getWinningHand() != null;
    }

    Collection<Domino> getWinningHand();

    void deal(List<Domino> subList);

    void showTable(Table table);

    void give(Domino removeFirst);

    Collection<Domino> offer(Domino offer);

    Domino discard();

    Collection<Domino> extractPairs();
}
