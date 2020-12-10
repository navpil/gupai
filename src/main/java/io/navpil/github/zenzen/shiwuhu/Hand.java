package io.navpil.github.zenzen.shiwuhu;

import io.navpil.github.zenzen.dominos.Domino;

import java.util.List;

public interface Hand {
    List<Domino> lead();

    String getName();

    void trick(List<Domino> lead);

    List<Domino> beat(List<Domino> lead);

    int getHu();

    int getLeftovers();
}
