package io.navpil.github.zenzen.mod10;

import io.navpil.github.zenzen.dominos.Domino;
import io.navpil.github.zenzen.dominos.DominoUtil;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

public class Mod10Rule {

    private static final Set<Domino> geeJoon = Set.of(Domino.of(4,2), Domino.of(2,1));

    private final boolean geeJoonWild;
    private final boolean zeroHigh;

    private Mod10Rule(boolean geeJoonWild, boolean zeroHigh) {
        this.geeJoonWild = geeJoonWild;
        this.zeroHigh = zeroHigh;
    }

    public static Mod10Rule kolYeSi() {
        return new Mod10Rule(false, false);
    }

    public static Mod10Rule daLing() {
        return new Mod10Rule(true, true);
    }

    public static Mod10Rule tauNgau() {
        return new Mod10Rule(true, false);
    }

    public Points getPoints(Collection<Domino> dominoes) {
        List<Integer> sumList = List.of(0);
        for (Domino d : dominoes) {
            final ArrayList<Integer> newSum = new ArrayList<>();
            final List<Integer> points = getPoints(d);
            for (Integer sum : sumList) {
                for (Integer oldPoints : points) {
                    newSum.add(sum + oldPoints);
                }
            }
            sumList = newSum;
        }
        return new Points(sumList, zeroHigh);
    }

    private List<Integer> getPoints(Domino domino) {
        if (geeJoonWild && isGeeJoon(domino)) {
            return List.of(3, 6);
        } else {
            return List.of(DominoUtil.getPipPoints(domino));
        }
    }

    private static boolean isGeeJoon(Domino domino) {
        return geeJoon.contains(domino);
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
