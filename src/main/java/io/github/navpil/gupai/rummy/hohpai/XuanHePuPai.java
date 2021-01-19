package io.github.navpil.gupai.rummy.hohpai;

import io.github.navpil.gupai.dominos.DominoUtil;
import io.github.navpil.gupai.dominos.Domino;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class XuanHePuPai {

    public enum Pairs {
        CHINESE(
                Set.of(
                        militaries(63, 54),
                        militaries(62, 53),
                        militaries(43, 52),
                        militaries(41, 32),
                        militaries(42, 21)
                )
        ),

        KOREAN(
                Set.of(
                        militaries(63, 62),
                        militaries(53, 52),
                        militaries(43, 42),
                        militaries(41, 32),
                        militaries(54, 21)
                )
        ),
        NONE(
                Collections.emptySet()
        );

        private final Set<Set<Domino>> militaries;
        private final Map<Domino, Domino> otherMap;

        Pairs(Set<Set<Domino>> militaries) {
            this.militaries = militaries;
            final HashMap<Domino, Domino> map = new HashMap<>();
            for (Set<Domino> military : militaries) {
                final Iterator<Domino> it = military.iterator();
                final Domino one = it.next();
                final Domino two = it.next();
                map.put(one, two);
                map.put(two, one);
            }
            otherMap = Collections.unmodifiableMap(map);
        }

        private static Set<Domino> militaries(int p1, int p2) {
            return Set.of(Domino.of(p1), Domino.of(p2));
        }


        public boolean validPairCombination(Collection<Domino> dominoes) {
            final List<Domino> civilsOnly = dominoes.stream().filter(Domino::isCivil).sorted().collect(Collectors.toList());
            if (civilsOnly.size() % 2 != 0) {
                return false;
            }
            for (int i = 0; i < civilsOnly.size(); i += 2) {
                if (!civilsOnly.get(i).equals(civilsOnly.get(i + 1))) {
                    return false;
                }
            }
            final Set<Domino> militaries = new HashSet<>(dominoes.stream().filter(Domino::isMilitary).collect(Collectors.toSet()));
            if (militaries.size() % 2 != 0) {
                return false;
            }
            for (Set<Domino> military : this.militaries) {
                final boolean b = militaries.removeAll(military);
                if (b && militaries.size() % 2 != 0) {
                    return false;
                }
            }
            return true;
        }

        public Domino other(Domino domino) {
            if (domino.isCivil()) {
                return domino;
            }
            return otherMap.get(domino);
        }
    }

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
    private final boolean includeThreePairs;

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

    public XuanHePuPai(
            boolean includeSok,
            boolean includeStraight,
            boolean includeThreePairs,
            boolean includeErSanKao,
            boolean includeMiddleFour,
            boolean includeFourteenAndFive,
            Pairs pairs) {
        this.includeSok = includeSok;
        this.includeStraight = includeStraight;
        this.includeThreePairs = includeThreePairs;
        this.includeErSanKao = includeErSanKao;
        this.includeMiddleFour = includeMiddleFour;
        this.includeFourteenAndFive = includeFourteenAndFive;
        this.pairs = pairs;
    }

    public static XuanHePuPai hoHpai(boolean includeSok) {
        return new XuanHePuPai(includeSok, true, true, true, false, false, Pairs.KOREAN);
    }

    public static XuanHePuPai hoHpai() {
        return hoHpai(true);
    }

    public static XuanHePuPai xiangShiFu() {
        return new XuanHePuPai(false, false, false, true, false, true, Pairs.CHINESE);
    }

    public static XuanHePuPai xiangShiFuModern() {
        return new XuanHePuPai(false, false, false, false, true, true, Pairs.CHINESE);
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
        THREE_PAIRS,

        //Pairs
        CIVIL_PAIR,

        MILITARY_CHINESE_PAIR,
        SUPREME_PAIR,

        MILITARY_KOREAN_PAIR,

        none
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
        } else if ((includeThreePairs || includeStraight) && dominos.size() == 6) {
            if (includeStraight) {
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
            if (includeThreePairs) {
                if (pairs.validPairCombination(dominos)) {
                    return Combination.THREE_PAIRS;
                }
            }
        } else if (pairs != Pairs.NONE && dominos.size() == 2) {
            final ArrayList<Domino> pair = new ArrayList<>(dominos);
            if (pair.get(0).isCivil() && pair.get(0).equals(pair.get(1))) {
                return Combination.CIVIL_PAIR;
            }
            if (pair.get(0).isMilitary()) {
                if (pairs == Pairs.KOREAN) {
                    if (pair.get(1).equals(KOREAN_MILITARY_PAIRS.get(pair.get(0)))) {
                        return Combination.MILITARY_KOREAN_PAIR;
                    }
                } else {
                    if (pair.get(1).equals(CHINESE_MILITARY_PAIRS.get(pair.get(0)))) {
                        return Combination.MILITARY_CHINESE_PAIR;
                    } else if (pair.get(1).equals(SUPREME_PAIR.get(pair.get(0)))) {
                        return Combination.SUPREME_PAIR;
                    }
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
