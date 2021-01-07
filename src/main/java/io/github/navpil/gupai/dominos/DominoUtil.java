package io.github.navpil.gupai.dominos;

import java.util.Set;

public class DominoUtil {

    private static final Set<Pair> chineseWuPairs = Set.of(
            new Pair(Domino.of(6, 3), Domino.of(5, 4)),
            new Pair(Domino.of(6, 2), Domino.of(5, 3)),
            new Pair(Domino.of(5, 2), Domino.of(4, 3)),
            new Pair(Domino.of(3, 2), Domino.of(4, 1))
    );

    private static final Set<Pair> koreanWuPairs = Set.of(
            new Pair(Domino.of(6, 3), Domino.of(6, 2)),
            new Pair(Domino.of(5, 3), Domino.of(5, 2)),
            new Pair(Domino.of(4, 3), Domino.of(4, 2)),

            new Pair(Domino.of(3, 2), Domino.of(4, 1)),

            new Pair(Domino.of(5, 4), Domino.of(2, 1))
    );

    private static final Pair supreme = new Pair(Domino.of(4, 2), Domino.of(2, 1));

    public static boolean isDouble(Domino domino) {
        return domino.getPips()[0] == domino.getPips()[1];
    }

    public static boolean containsPip(Domino domino, int pip) {
        final int[] pips = domino.getPips();
        return pips[0] == pip || pips[1] == pip;
    }

    public static boolean isChinesePair(Domino d1, Domino d2) {
        return isWenPair(d1, d2) || isChineseWuPair(d1, d2) || isSupremePair(d1, d2);
    }

    private static boolean isSupremePair(Domino d1, Domino d2) {
        return supreme.equals(new Pair(d1, d2));
    }

    private static boolean isChineseWuPair(Domino d1, Domino d2) {
        return chineseWuPairs.contains(new Pair(d1, d2));
    }

    public static boolean isKoreanPair(Domino d1, Domino d2) {
        return isWenPair(d1, d2) || isKoreanWuPair(d1, d2);
    }

    private static boolean isKoreanWuPair(Domino d1, Domino d2) {
        return koreanWuPairs.contains(new Pair(d1, d2));
    }

    public static boolean isWenPair(Domino d1, Domino d2) {
        return d1.isCivil() && d2.isCivil() && d1.equals(d2);
    }

    public static int getPipPoints(Domino d) {
        return d.getPips()[0] + d.getPips()[1];
    }

}
