package io.github.navpil.gupai;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class XuanHePaiPu {

    /**
     * Sok are COINCIDENCE and FIVE_SONS - sometimes not used in a Ho-Hpai game
     */
    private final boolean includeSok;

    /**
     * Special Ho-Hpai combination (e.g. [3:1][3:2][3:3][3:4][3:5][3:6]
     */
    private final boolean includeStraight;

    /**
     * According to Culin 3 pairs is a valid combination in Ho-Hpai, if I read him correctly
     */
    private final Pairs threePairsRule;

    /**
     * 2-2-3-3-6-6 may be replaced by 2-3-3-4-4-5 in some XiangShiFu rules
     */
    private final boolean includeErSanKao;
    /**
     * 2-3-3-4-4-5 used in some XiangShiFu rules
     */
    private final boolean includeMiddleFour;

    /**
     * Ho-hpai does not use SPLENDID and FIVE_POINTS
     */
    private final boolean includeFourteenAndFive;

    /**
     * Which kind of pairs is used in a game - Korean, Chinese or none
     */
    private final Pairs pairs;

    public XuanHePaiPu(
            boolean includeSok,
            boolean includeStraight,
            Pairs threePairsRule,
            boolean includeErSanKao,
            boolean includeMiddleFour,
            boolean includeFourteenAndFive,
            Pairs pairs) {
        this.includeSok = includeSok;
        this.includeStraight = includeStraight;
        this.threePairsRule = threePairsRule;
        this.includeErSanKao = includeErSanKao;
        this.includeMiddleFour = includeMiddleFour;
        this.includeFourteenAndFive = includeFourteenAndFive;
        this.pairs = pairs;
    }

    /**
     * 3 Korean pairs are a valid combination, no splendid and five points, 2 tiles are not a valid combination, so no pairs.
     * Includes HoHpai straights
     *
     * @param includeSok whether to include five sons and coincidence
     * @return rules calculation for HoHpai
     */
    public static XuanHePaiPu hoHpai(boolean includeSok) {
        return new XuanHePaiPu(includeSok, true, Pairs.KOREAN, true, false, false, Pairs.NONE);
    }

    /**
     * No splendid and five points, 2 tiles are not a valid combination, so no pairs
     * Includes HoHpai straights.
     *
     * @return rules calculation for Tok
     */
    public static XuanHePaiPu tok() {
        return new XuanHePaiPu(true, true, Pairs.NONE, true, false, false, Pairs.NONE);
    }

    /**
     * Classical XiangShiFu, all XuanHePuPai triplets + chinese pairs
     *
     * @return classical XiangShiFu rules calculation
     */
    public static XuanHePaiPu xiangShiFu() {
        return new XuanHePaiPu(false, false, Pairs.NONE, true, false, true, Pairs.CHINESE);
    }

    /**
     * Modern XiangShiFu, 2-3-6 is replaced with a middle dragon 2-3-3-4-4-5
     *
     * @return classical XiangShiFu rules calculation
     */
    public static XuanHePaiPu xiangShiFuModern() {
        return new XuanHePaiPu(false, false, Pairs.NONE, false, true, true, Pairs.CHINESE);
    }

    /**
     *                    [4:5] [3:6]
     *                    [3:5] [2:6]
     *                    [3:4] [2:5]
     *                    [1:4] [2:3]
     */
    private static final HashMap<Domino, Domino> CHINESE_MILITARY_PAIRS = new HashMap<>();
    /**
     *                    [2:1] [4:2]
     */
    private static final HashMap<Domino, Domino> SUPREME_PAIR = new HashMap<>();

    /**
     *                    [2:6] [3:6]
     *                    [2:5] [3:5]
     *                    [2:4] [3:4]
     *                    [1:4] [2:3]
     *                    [1:2] [4:5]
     */
    private static final HashMap<Domino, Domino> KOREAN_MILITARY_PAIRS = new HashMap<>();


    static {
        //Korean military pairs:
        final List<List<Domino>> korean = List.of(
                Domino.ofList(62, 63),
                Domino.ofList(52, 53),
                Domino.ofList(42, 43),
                Domino.ofList(14, 23),
                Domino.ofList(12, 45)
        );
        for (List<Domino> pairList : korean) {
            KOREAN_MILITARY_PAIRS.put(pairList.get(0), pairList.get(1));
            KOREAN_MILITARY_PAIRS.put(pairList.get(1), pairList.get(0));
        }

        //Korean military pairs:
        final List<List<Domino>> chinese = List.of(
                Domino.ofList(54, 63),
                Domino.ofList(53, 62),
                Domino.ofList(52, 43),
                Domino.ofList(14, 23)
        );
        for (List<Domino> pairList : chinese) {
            CHINESE_MILITARY_PAIRS.put(pairList.get(0), pairList.get(1));
            CHINESE_MILITARY_PAIRS.put(pairList.get(1), pairList.get(0));
        }

        //Supreme
        final List<Domino> supremes = Domino.ofList(42, 21);
        SUPREME_PAIR.put(supremes.get(0), supremes.get(1));
        SUPREME_PAIR.put(supremes.get(1), supremes.get(0));


    }

    public CombinationType evaluate(Collection<Domino> dominos) {
        if (dominos.size() == 3) {
            int[] pips = new int[6];
            int counter = 0;
            for (Domino domino : dominos) {
                final int[] p = domino.getPips();
                pips[counter++] = p[0];
                pips[counter++] = p[1];
            }
            return isTriplet(pips);
        } else if ((threePairsRule != Pairs.NONE || includeStraight) && dominos.size() == 6) {
            if (includeStraight) {
                final List<Domino> doubles = dominos.stream().filter(DominoUtil::isDouble).collect(Collectors.toList());
                if (doubles.size() == 1) {
                    final int pip = doubles.get(0).getPips()[0];
                    int[] pips = new int[7];
                    for (Domino domino : dominos) {
                        pips[other(domino.getPips(), pip)]++;
                    }
                    if (Arrays.equals(pips, new int[]{0, 1, 1, 1, 1, 1, 1})) {
                        return CombinationType.STRAIGHT;
                    }
                }
            }
            if (threePairsRule.validAllPairsCombination(dominos)) {
                return CombinationType.THREE_PAIRS;
            }
        } else if (pairs != Pairs.NONE && dominos.size() == 2) {
            final ArrayList<Domino> pair = new ArrayList<>(dominos);
            if (pair.get(0).isCivil() && pair.get(0).equals(pair.get(1))) {
                return getCivilPairType(pair.get(0));
            }
            if (pair.get(0).isMilitary()) {
                if (pairs == Pairs.KOREAN) {
                    if (pair.get(1).equals(KOREAN_MILITARY_PAIRS.get(pair.get(0)))) {
                        return CombinationType.MILITARY_KOREAN_PAIR;
                    }
                } else {
                    if (pair.get(1).equals(CHINESE_MILITARY_PAIRS.get(pair.get(0)))) {
                        return getMilitaryPairType(pair.get(0));
                    } else if (pair.get(1).equals(SUPREME_PAIR.get(pair.get(0)))) {
                        return CombinationType.SUPREME_PAIR;
                    }
                }

            }
        }
        return CombinationType.none;
    }

    private CombinationType getMilitaryPairType(Domino domino) {
        int pipSum = domino.getPips()[0] + domino.getPips()[1];
        if (pipSum == 9) {
            return CombinationType.NINES;
        } else if (pipSum == 8) {
            return CombinationType.EIGHTS;
        } else if (pipSum == 7) {
            return CombinationType.SEVENS;
        } else if (pipSum == 5) {
            return CombinationType.FIVES;
        }
        throw new IllegalArgumentException("This domino does not belong to any Military pair " + domino);
    }

    private CombinationType getCivilPairType(Domino domino) {
        if (domino.is(6, 6)) {
            return CombinationType.HEAVEN;
        } else if (domino.is(1, 1)) {
            return CombinationType.EARTH;
        } else if (domino.is(4, 4)) {
            return CombinationType.MAN;
        } else if (domino.is(1, 3)) {
            return CombinationType.HARMONY;
        } else if (domino.is(5, 5)) {
            return CombinationType.PLUM;
        } else if (domino.is(3, 3)) {
            return CombinationType.LONG_THREE;
        } else if (domino.is(2, 2)) {
            return CombinationType.BENCH;
        } else if (domino.is(6, 5)) {
            return CombinationType.AXE;
        } else if (domino.is(6, 4)) {
            return CombinationType.RED_HEAD;
        } else if (domino.is(6, 1)) {
            return CombinationType.LONG_LEGS;
        } else if (domino.is(5, 1)) {
            return CombinationType.BIG_HEAD;
        }
        throw new IllegalArgumentException("This domino does not belong to any Civil pair " + domino);
    }

    private int other(int[] pips, int pip) {
        if (pips[0] == pip) return pips[1];
        if (pips[1] == pip) return pips[0];
        return 0;
    }

    private CombinationType isTriplet(int[] pips) {
        Arrays.sort(pips);
        if (includeErSanKao && exactly(pips, 2, 2, 3, 3, 6, 6)) {
            return CombinationType.ER_SAN_KAO;
        }
        if (includeMiddleFour && exactly(pips, 2, 3, 3, 4, 4, 5)) {
            return CombinationType.MIDDLE_DRAGON;
        }
        if (exactly(pips, 1, 1, 2, 2, 3, 3)) {
            return CombinationType.SMALL_THREE;
        }
        if (exactly(pips, 4, 4, 5, 5, 6, 6)) {
            return CombinationType.BIG_THREE;
        }
        if (exactly(pips, 1, 2, 3, 4, 5, 6)) {
            return CombinationType.DRAGON;
        }
        if (includeSok && fiveSons(pips)) {
            return CombinationType.FIVE_SONS;
        }
        if (split(pips)) {
            return CombinationType.SPLIT;
        }
        int totalSum = 0;
        for (int pip : pips) {
            totalSum += pip;
        }
        int number = findFourRun(pips);
        if (number > 0 && totalSum == number * 5) {
            if (!includeSok) {
                return CombinationType.none;
            }
            if (totalSum == number * 5) {
                return CombinationType.COINCIDENCE;
            }
            //This is a special case, because otherwise 3-3 3-3 1-1 may wrongly be considered as FIVE_POINTS (3-3-3 3+1+1)
            // or 6-6 6-6 4-4 be considered as SPLENDID (6-6-6 6+4+4)
            return CombinationType.none;
        }
        if (!includeFourteenAndFive) {
            return CombinationType.none;
        }
        number = findThreeRun(pips);
        if (number > 0) {
            final int remainder = totalSum - (number * 3);
            if (remainder == 5) {
                return CombinationType.FIVE_POINTS;
            } else if (remainder >= 14) {
                return CombinationType.SPLENDID;
            }
        }
        return CombinationType.none;
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
