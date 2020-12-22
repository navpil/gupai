package io.navpil.github.zenzen.dominos;

public class DominoUtil {

    public static boolean isDouble(Domino domino) {
        return domino.getPips()[0] == domino.getPips()[1];
    }

    public static boolean containsPip(Domino domino, int pip) {
        final int[] pips = domino.getPips();
        return pips[0] == pip || pips[1] == pip;
    }

}
