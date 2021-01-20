package io.github.navpil.gupai;

public class DominoUtil {

    public static boolean isDouble(Domino domino) {
        return domino.getPips()[0] == domino.getPips()[1];
    }

    public static boolean containsPip(Domino domino, int pip) {
        final int[] pips = domino.getPips();
        return pips[0] == pip || pips[1] == pip;
    }

    public static boolean isWenPair(Domino d1, Domino d2) {
        return d1.isCivil() && d2.isCivil() && d1.equals(d2);
    }

    public static int getPipPoints(Domino d) {
        return d.getPips()[0] + d.getPips()[1];
    }

    public static int redPointsCount(Domino d) {
        final int[] pips = d.getPips();
        if (d.is(6,6)) {
            //[6:6] is drawn with 6 red pips
            return 6;
        }
        return (isRed(pips[0]) ? pips[0] : 0) + (isRed(pips[1]) ? pips[1] : 0);
    }

    public static int blackPointsCount(Domino d) {
        return getPipPoints(d) - redPointsCount(d);
    }

    private static boolean isRed(int pip) {
        return pip == 1 || pip == 4;
    }

}
