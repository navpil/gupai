package io.github.navpil.gupai.fishing;

import io.github.navpil.gupai.fishing.tsungshap.NamedPlayer;
import io.github.navpil.gupai.dominos.Domino;
import io.github.navpil.gupai.util.Bag;

import java.util.Collection;

public interface Player extends NamedPlayer {
    void deal(Collection<Domino> deal);

    void showTable(Table table);

    String getName();

    /**
     * Fish with the bait from hand
     *
     * @param pool pool where to fish from
     * @return catch which includes bait and fish
     */
    Catch fish(Bag<Domino> pool);

    /**
     * Fish with the bait from woodpile
     *
     * @param pool pool where to fish from
     * @param bait given bait to fish with
     * @return catch which includes bait and fish
     */
    Catch fish(Bag<Domino> pool, Domino bait);
}
