package io.github.navpil.gupai.mod10;

import io.github.navpil.gupai.dominos.DominoUtil;
import io.github.navpil.gupai.dominos.Domino;

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
     * 0 is high
     */
    DA_LING(true, true),
    /**
     * Pai gow and Tau ngau rules.
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

    public static class Points {

        private final List<Integer> sums;
        private final boolean zeroHigh;

        private Points(List<Integer> sums, boolean zeroHigh) {
            this.sums = sums;
            this.zeroHigh = zeroHigh;
        }

        public Integer getMax() {
            return sums.stream().map(i -> i % 10).map(i -> zeroHigh && i == 0  ? 10 : i).max(Integer::compareTo).orElseThrow();
        }

        public boolean isMod10() {
            return sums.stream().map(i -> i % 10).anyMatch(i -> i == 0);
        }

    }


}
