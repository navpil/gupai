package io.github.navpil.gupai.shiwuhu;

import io.github.navpil.gupai.dominos.Domino;

import java.util.List;

public interface Player {
    List<Domino> lead();

    String getName();

    void trick(List<Domino> lead);

    List<Domino> beat(List<Domino> lead);

    int getHu();

    int getLeftovers();
}
