package io.navpil.github.zenzen.fishing;

import io.navpil.github.zenzen.dominos.Domino;
import io.navpil.github.zenzen.fishing.tsungshap.NamedPlayer;
import io.navpil.github.zenzen.util.Bag;

import java.util.Collection;

public interface Player extends NamedPlayer {
    void deal(Collection<Domino> deal);

    void showTable(Table table);

    String getName();

    Catch fish(Bag<Domino> pool);

    Catch fish(Bag<Domino> pool, Domino bait);
}
