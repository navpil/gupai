package io.github.navpil.gupai.rummy.kapshap;

import io.github.navpil.gupai.dominos.Pair;
import io.github.navpil.gupai.dominos.Domino;

public class Mod10Pair extends Pair {

    private final int mod10;

    public Mod10Pair(Domino d1, Domino d2) {
        super(d1, d2);
        mod10 = Mod10Calculation.mod10(d1, d2);
    }
}
