package io.navpil.github.zenzen.mod10;

import io.navpil.github.zenzen.dominos.Domino;

import java.util.Arrays;
import java.util.Collection;

public class Mod10Calculation {

    public static int mod10(Collection<Domino> dominoes) {
        return dominoes.stream().map(domino -> domino.getPips()[0] + domino.getPips()[1]).reduce(Integer::sum).orElse(0) % 10;
    }

    public static int mod10(Domino ... dominoes) {
        return mod10(Arrays.asList(dominoes));
    }

}
