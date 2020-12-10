package io.navpil.github.zenzen;

import io.navpil.github.zenzen.dominos.IDomino;
import io.navpil.github.zenzen.dominos.Domino;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ZenZenRules {

    private static final Map<Set<IDomino>, Combination> pairPotentialCache = new HashMap<>();

    public static Combination calculatePairPotential(List<IDomino> dominoes) {
        for (IDomino domino : dominoes) {
            if (domino.getPips().length == 0) {
                return Combination.none;
            }
        }
        final Domino first = (Domino) dominoes.get(0);
        final Domino second = (Domino) dominoes.get(1);
        Combination pair = isPair(first, second);
        if (pair != Combination.none) {
            return pair;
        }
        final Domino third = (Domino) dominoes.get(2);
        pair = isPair(first, third);
        if (pair != Combination.none) {
            return pair;
        }
        return isPair(second, third);
    }

    public static Combination calculateCombination(List<? extends IDomino> dominoes) {
        int totalSize = 0;
        for (IDomino dominoe : dominoes) {
            totalSize += dominoe.getPips().length;
        }
        if (totalSize < 4) {
            //not a pair, not a triple, invalid move - triplet has less than two dominoes
            return Combination.none;
        }
        if (totalSize == 4) {
            final ArrayList<Domino> dominos = new ArrayList<>();
            for (IDomino dominoe : dominoes) {
                if (dominoe.getPips().length != 0) {
                    dominos.add((Domino)dominoe);
                }
            }
            return isPair(dominos.get(0), dominos.get(1));
        }
        final int[] allPips = new int[totalSize];
        int counter = 0;
        for (IDomino dominoe : dominoes) {
            final int[] pips = dominoe.getPips();
            allPips[counter++] = pips[0];
            allPips[counter++] = pips[1];
        }

        return isTriplet(allPips);
    }

    private static Combination isPair(Domino first, Domino second) {
        if (first.isCivil() && second.isCivil()) {
            if  (first.equals(second)) {
                if (first.is(6, 6)) {
                    return Combination.HEAVEN;
                } else if (first.is(1, 1)) {
                    return Combination.EARTH;
                } else if (first.is(4, 4)) {
                    return Combination.MAN;
                } else if (first.is(1, 3)) {
                    return Combination.HARMONY;
                } else if (first.is(5, 5)) {
                    return Combination.PLUM;
                } else if (first.is(3, 3)) {
                    return Combination.LONG_THREE;
                } else if (first.is(2, 2)) {
                    return Combination.BENCH;
                } else if (first.is(6, 5)) {
                    return Combination.AXE;
                } else if (first.is(6, 4)) {
                    return Combination.RED_HEAD;
                } else if (first.is(6, 1)) {
                    return Combination.LONG_LEGS;
                } else if (first.is(5, 1)) {
                    return Combination.BIG_HEAD;
                }
                return Combination.GENERIC_CIVIL_PAIR;
            }
        } else if (first.isMilitary() && second.isMilitary()) {
            final int[] pips1 = first.getPips();
            final int[] pips2 = second.getPips();
            final int sum1 = pips1[0] + pips1[1];
            final int sum2 = pips2[0] + pips2[1];
            boolean samePips = sum1 == sum2;
            if (samePips) {
                if (sum1 == 9) {
                    return Combination.NINES;
                } else if (sum1 == 8) {
                    return Combination.EIGHTS;
                } else if (sum1 == 7) {
                    return Combination.SEVENS;
                } else if (sum1 == 5) {
                    return Combination.FIVES;
                }
                return Combination.GENERIC_MILITARY_PAIR;
            }
            //Check supreme pair
            if ((sum1 == 3 && sum2 == 6) || (sum1 == 6 && sum2 == 3)) {
                return Combination.SUPREME;
            }
        }
        return Combination.none;
    }

    private static Combination isTriplet(int [] pips) {
        Arrays.sort(pips);
        if (exactly(pips, 2, 3, 3, 4, 4, 5)) {
            return Combination.MIDDLE_FOUR;
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
        if (fiveSons(pips)) {
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
            if (totalSum == number * 5) {
                return Combination.COINCIDENCE;
            }
            //This is a special case, because otherwise 3-3 3-3 1-1 may wrongly be considered as FIVE_POINTS (3-3-3 3+1+1)
            // or 6-6 6-6 4-4 be considered as SPLENDID (6-6-6 6+4+4)
            //It's not clear whether above sequences are valid combinations, but I assume that no.
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
