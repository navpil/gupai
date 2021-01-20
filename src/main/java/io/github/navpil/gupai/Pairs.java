package io.github.navpil.gupai;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Used as an enum, but also can check whether a hand consists of only pairs.
 *
 * Not all games need a distinction between military/supremes/civil or other XuanHePuPai
 */
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
    private final Map<Domino, Domino> otherMilitaryMap;

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
        otherMilitaryMap = Collections.unmodifiableMap(map);
    }

    private static Set<Domino> militaries(int p1, int p2) {
        return Set.of(Domino.of(p1), Domino.of(p2));
    }


    public boolean validAllPairsCombination(Collection<Domino> dominoes) {
        if (this == NONE) {
            return false;
        }
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

    /**
     * Returns another domino from the pair
     * @param domino domino to search pair for
     * @return pair for a domino
     */
    public Domino other(Domino domino) {
        if (domino.isCivil()) {
            return domino;
        }
        return otherMilitaryMap.get(domino);
    }
}
