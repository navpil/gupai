package io.github.navpil.gupai.mod10;

import io.github.navpil.gupai.dominos.Domino;

import java.util.Arrays;
import java.util.Collection;

/**
 * @deprecated
 *
 * Consider using Mod10Rule instead
 */
public class Mod10Calculation {

    static int mod10(Collection<Domino> dominoes) {
        return dominoes.stream().map(domino -> domino.getPips()[0] + domino.getPips()[1]).reduce(Integer::sum).orElse(0) % 10;
    }

    static int mod10(Domino ... dominoes) {
        return mod10(Arrays.asList(dominoes));
    }

}
