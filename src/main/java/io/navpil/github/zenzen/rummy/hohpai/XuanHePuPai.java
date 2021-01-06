package io.navpil.github.zenzen.rummy.hohpai;

import io.navpil.github.zenzen.dominos.Domino;
import io.navpil.github.zenzen.dominos.DominoUtil;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class XuanHePuPai {

    private final boolean includeSok;
    private final boolean includeStraight;
    private final boolean includeErSanKao;
    private final boolean includeMiddleFour;
    private final boolean includeFourteenAndFive;

    public XuanHePuPai(
            boolean includeSok,
            boolean includeStraight,
            boolean includeErSanKao,
            boolean includeMiddleFour,
            boolean includeFourteenAndFive) {
        this.includeSok = includeSok;
        this.includeStraight = includeStraight;
        this.includeErSanKao = includeErSanKao;
        this.includeMiddleFour = includeMiddleFour;
        this.includeFourteenAndFive = includeFourteenAndFive;
    }

    public static XuanHePuPai hoHpai(boolean includeSok) {
        return new XuanHePuPai(includeSok, true, true, false, false);
    }

    public static XuanHePuPai hoHpai() {
        return hoHpai(true);
    }

    public static XuanHePuPai xiangShiFu() {
        return new XuanHePuPai(false, false, true, false, true);
    }

    public static XuanHePuPai xiangShiFuModern() {
        return new XuanHePuPai(false, false, false, true, true);
    }

    public enum Combination {

        //ssang-syo - all different
        DRAGON,

        //tai-sam-tong
        SPLIT,

        //sok (coincidence and five sons)
        FIVE_SONS,
        COINCIDENCE,

        //ssang-pyen (Doublets?)
        ER_SAN_KAO,
        //a-ki Child
        SMALL_THREE,
        //ro-in Old Man
        BIG_THREE,


        //MIDDLE_DRAGON -> used instead of er-san-kao
        MIDDLE_DRAGON,

        //Used in XiangShiFu - not used for HoHpai
        FIVE_POINTS,
        SPLENDID,

        //Ho-hpai specials
        STRAIGHT,

        none
    }

    public Combination evaluate(Collection<Domino> dominos) {
        if (dominos.size() == 3) {
            int[] pips = new int[6];
            int counter = 0;
            for (Domino domino : dominos) {
                final int[] p = domino.getPips();
                pips[counter++] = p[0];
                pips[counter++] = p[1];
            }
            return isTriplet(pips);
        } else if (includeStraight && dominos.size() == 6) {
            final List<Domino> doubles = dominos.stream().filter(DominoUtil::isDouble).collect(Collectors.toList());
            if (doubles.size() == 1) {
                final int pip = doubles.get(0).getPips()[0];
                int[] pips = new int[7];
                for (Domino domino : dominos) {
                    pips[other(domino.getPips(), pip)]++;
                }
                if (Arrays.equals(pips, new int[]{0, 1, 1, 1, 1, 1, 1})) {
                    return Combination.STRAIGHT;
                }
            }
        }
        return Combination.none;
    }

    private int other(int[] pips, int pip) {
        if (pips[0] == pip) return pips[1];
        if (pips[1] == pip) return pips[0];
        return 0;
    }

    private Combination isTriplet(int[] pips) {
        Arrays.sort(pips);
        if (includeErSanKao && exactly(pips, 2, 2, 3, 3, 6, 6)) {
            return Combination.ER_SAN_KAO;
        }
        if (includeMiddleFour && exactly(pips, 2, 3, 3, 4, 4, 5)) {
            return Combination.MIDDLE_DRAGON;
        }
        if (exactly(pips, 1, 1, 2, 2, 3, 3)) {
            return Combination.SMALL_THREE;
        }
        if (exactly(pips, 4, 4, 5, 5, 6, 6)) {
            return Combination.BIG_THREE;
        }
        if (exactly(pips, 1, 2, 3, 4, 5, 6)) {
            return Combination.DRAGON;
        }
        if (includeSok && fiveSons(pips)) {
            return Combination.FIVE_SONS;
        }
        if (split(pips)) {
            return Combination.SPLIT;
        }
        int totalSum = 0;
        for (int pip : pips) {
            totalSum += pip;
        }
        int number = findFourRun(pips);
        if (number > 0 && totalSum == number * 5) {
            if (!includeSok) {
                return Combination.none;
            }
            if (totalSum == number * 5) {
                return Combination.COINCIDENCE;
            }
            //This is a special case, because otherwise 3-3 3-3 1-1 may wrongly be considered as FIVE_POINTS (3-3-3 3+1+1)
            // or 6-6 6-6 4-4 be considered as SPLENDID (6-6-6 6+4+4)
            //It's not clear whether above sequences are valid combinations, but I assume that no.
            return Combination.none;
        }
        if (!includeFourteenAndFive) {
            return Combination.none;
        }
        number = findThreeRun(pips);
        if (number > 0) {
            final int remainder = totalSum - (number * 3);
            if (remainder == 5) {
                return Combination.FIVE_POINTS;
            } else if (remainder >= 14) {
                return Combination.SPLENDID;
            }
        }
        return Combination.none;
    }

    private static int findFourRun(int[] pips) {
        for (int i = 0; i <= 2; i++) {
            if (pips[i] == pips[i + 3]) {
                return pips[i];
            }
        }
        return -1;
    }

    private static int findThreeRun(int[] pips) {
        for (int i = 0; i <= 3; i++) {
            if (pips[i] == pips[i + 2]) {
                return pips[i];
            }
        }
        return -1;
    }

    private static boolean split(int[] pips) {
        return pips[0] == pips[2] && pips[3] == pips[5];
    }

    private static boolean fiveSons(int[] pips) {
        return pips[0] == pips[4] || pips[1] == pips[5];
    }

    private static boolean exactly(int[] pips, int i1, int i2, int i3, int i4, int i5, int i6) {
        return pips[0] == i1 &&
                pips[1] == i2 && pips[2] == i3 &&
                pips[3] == i4 && pips[4] == i5 &&
                pips[5] == i6;
    }


}
