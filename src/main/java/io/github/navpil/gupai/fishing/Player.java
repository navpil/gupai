package io.github.navpil.gupai.fishing;

import io.github.navpil.gupai.fishing.tsungshap.NamedPlayer;
import io.github.navpil.gupai.dominos.Domino;
import io.github.navpil.gupai.util.Bag;

import java.util.Collection;

public interface Player extends NamedPlayer {
    void deal(Collection<Domino> deal);

    void showTable(Table table);

    String getName();

    Catch fish(Bag<Domino> pool);

    Catch fish(Bag<Domino> pool, Domino bait);
}
