package io.github.navpil.gupai.xiangshifu;

import io.github.navpil.gupai.CombinationType;
import io.github.navpil.gupai.Domino;
import io.github.navpil.gupai.IDomino;
import io.github.navpil.gupai.XuanHePaiPu;

import java.util.List;
import java.util.stream.Collectors;

public class XiangShiFuRules {

    /**
     * This does not look nice, injecting stuff into a static fields is against best practices,
     * but this solution looks good for now - we do not plan to run this game in multiple threads with different rules.
     */
    private static XuanHePaiPu XUAN_HE_PAI_PU = XuanHePaiPu.xiangShiFuModern();

    public static void changeRules(XuanHePaiPu xhpp) {
        XUAN_HE_PAI_PU = xhpp;
    }

    /**
     * Pair potential means that these three dominoes contain a pair in them.
     * In case one of the dominoes is NoDomino (three dominoes represent a pair) then the pair potential is none.
     *
     * @param dominoes 3 dominos to check whether they contain a pair
     * @return Pair type
     */
    public static CombinationType calculatePairPotential(List<IDomino> dominoes) {
        final List<Domino> collect = dominoes.stream().filter(d -> d instanceof Domino).map(d -> (Domino) d).collect(Collectors.toList());
        if (collect.size() != 3) {
            return CombinationType.none;
        }

        final Domino first = (Domino) dominoes.get(0);
        final Domino second = (Domino) dominoes.get(1);
        CombinationType pair = XUAN_HE_PAI_PU.evaluate(List.of(first, second));
        if (pair != CombinationType.none) {
            return pair;
        }
        final Domino third = (Domino) dominoes.get(2);
        pair =  XUAN_HE_PAI_PU.evaluate(List.of(first, third));
        if (pair != CombinationType.none) {
            return pair;
        }
        return XUAN_HE_PAI_PU.evaluate(List.of(second, third));
    }

    /**
     * Calculate a combination from a given dominoes
     *
     * @param dominoes 3 dominoes
     * @return pair or triplet type if any
     */
    public static CombinationType calculateCombination(List<? extends IDomino> dominoes) {
        final List<Domino> collect = dominoes.stream().filter(d -> d instanceof Domino).map(d -> (Domino) d).collect(Collectors.toList());
        return XUAN_HE_PAI_PU.evaluate(collect);
    }

}
