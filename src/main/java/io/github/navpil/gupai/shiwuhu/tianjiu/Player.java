package io.github.navpil.gupai.shiwuhu.tianjiu;

import io.github.navpil.gupai.dominos.Domino;
import io.github.navpil.gupai.fishing.tsungshap.NamedPlayer;

import java.util.Collection;

public interface Player extends NamedPlayer {

    String getName();

    void deal(Collection<Domino> deal);

    void showTable(Table table);

    Collection<Domino> lead();

    Collection<Domino> beat(Collection<Domino> lead);

    Collection<Domino> discard(Collection<Domino> discard);

}
