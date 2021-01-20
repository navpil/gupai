package io.github.navpil.gupai.mod10;

import io.github.navpil.gupai.DominoUtil;
import io.github.navpil.gupai.Domino;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

public enum Mod10Rule {

    /**
     * Korean game, so Supreme cards have no significance
     */
    KOL_YE_SI(false, false),

    /**
     * 0 is high (meaning is equal to 10)
     */
    DA_LING(true, true),

    /**
     * 0 is high, but supremes are not wild (for bankers pair calculation only)
     */
    DA_LING_BANKER(false, true),

    /**
     * Pai gow and Tau ngau rules. Supremes are wild, but 0 is low
     */
    TAU_NGAU(true, false);

    public static final Set<Domino> SUPREMES = Set.of(Domino.of(4,2), Domino.of(2,1));

    private final boolean geeJoonWild;
    private final boolean zeroHigh;

    Mod10Rule(boolean geeJoonWild, boolean zeroHigh) {
        this.geeJoonWild = geeJoonWild;
        this.zeroHigh = zeroHigh;
    }

    public Points getPoints(Domino domino) {
        return getPoints(List.of(domino));
    }

    public Points getPoints(Collection<Domino> dominoes) {
        List<Integer> sumList = List.of(0);
        for (Domino d : dominoes) {
            final ArrayList<Integer> newSum = new ArrayList<>();
            final List<Integer> points = getPointsList(d);
            for (Integer sum : sumList) {
                for (Integer oldPoints : points) {
                    newSum.add(sum + oldPoints);
                }
            }
            sumList = newSum;
        }
        return new Points(sumList, zeroHigh);
    }

    private List<Integer> getPointsList(Domino domino) {
        if (geeJoonWild && isGeeJoon(domino)) {
            return List.of(3, 6);
        } else {
            return List.of(DominoUtil.getPipPoints(domino));
        }
    }

    private static boolean isGeeJoon(Domino domino) {
        return SUPREMES.contains(domino);
    }

    /**
     * Because Supremes may be wild, same combination may be divisible by 10 and also may cost 6 points.
     *
     * For example [2:2] [2:1] [4:2] is divisible by 10 (4 + 3 + 3) and may cost 6 points (4 + 6 + 6)
     */
    public static class Points {

        private final List<Integer> sums;
        private final boolean zeroHigh;

        private Points(List<Integer> sums, boolean zeroHigh) {
            this.sums = sums;
            this.zeroHigh = zeroHigh;
        }

        /**
         * Finds the maximum possible value of the possible sums
         *
         * @return maximum possible value
         */
        public Integer getMax() {
            return sums.stream().map(i -> i % 10).map(i -> zeroHigh && i == 0  ? 10 : i).max(Integer::compareTo).orElseThrow();
        }

        /**
         * Checks if a combination is divisible by 10
         *
         * @return true if divisible by 10, false otherwise
         */
        public boolean isMod10() {
            return sums.stream().map(i -> i % 10).anyMatch(i -> i == 0);
        }

        /**
         * Checks if a combination can be exactly equal to the provided point.
         * Used in DaLing - exact point game.
         *
         * @param point point to check against
         * @return true if it can match, false otherwise
         */
        public boolean canBeExactly(int point) {
            return sums.stream().map(i -> i % 10).map(i -> zeroHigh && i == 0  ? 10 : i).anyMatch(i -> i == point);
        }

    }


}
