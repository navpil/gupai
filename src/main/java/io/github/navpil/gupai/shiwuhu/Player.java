package io.github.navpil.gupai.shiwuhu;

import io.github.navpil.gupai.dominos.Domino;
import io.github.navpil.gupai.fishing.tsungshap.NamedPlayer;

import java.util.Collection;
import java.util.List;

public interface Player extends NamedPlayer {

    void deal(Collection<Domino> deal);

    List<Domino> lead();

    String getName();

    void trick(List<Domino> lead);

    List<Domino> beat(List<Domino> lead);

    int getHu();

    int getLeftovers();
}
