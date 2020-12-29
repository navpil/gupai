package io.navpil.github.zenzen.rummy.kapshap;

import io.navpil.github.zenzen.dominos.Domino;

import java.util.Arrays;
import java.util.Collection;

public class Mod10Calculation {

    //Special KapShap case
    private static final Domino fourTwo = Domino.of(4,2);

    public static int mod10(Collection<Domino> dominoes) {
        return dominoes.stream().map(domino -> fourTwo.equals(domino) ? 3 : domino.getPips()[0] + domino.getPips()[1]).reduce(Integer::sum).orElse(0) % 10;
    }

    public static int mod10(Domino ... dominoes) {
        return mod10(Arrays.asList(dominoes));
    }

}
