package io.github.navpil.gupai.rummy.jjakmatchugi;

import io.github.navpil.gupai.Domino;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class RuleSet {

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
     * Military pairs may be CHINESE or KOREAN
     */
    private final Pairs pairsType;

    /**
     * Players are forced to put the completed pairs in front of them.
     *
     * Theoretically this does not affect the gameplay, but some AI implementations benefit from the fact that
     * pairs are not used in 'discard' calculation anymore (this is especially true for a Random-based AI)
     */
    private final boolean forceShowPairs;

    /**
     * Original Jjak-mat-chu-gi, described by Culin, is a very drawish game - most of the games for 4 players result
     * in no one winning.
     * If the MahJong-like rule is introduced - offer the last discarded tile to every player to from a combination -
     * then the game ends in a win much more often.
     *
     * There are other ways, for example allow a player to pick any discarded tile, not only the last one, similar to
     * KapShap, but with a forced combination, but other solutions are not yet included.
     */
    private final boolean offerToAll;

    public RuleSet(Pairs pairsType, boolean forceShowPairs, boolean offerToAll) {
        this.pairsType = pairsType;
        this.forceShowPairs = forceShowPairs;
        this.offerToAll = offerToAll;
    }

    public static RuleSet culin() {
        return new RuleSet(Pairs.KOREAN, true, false);
    }

    public static RuleSet mahJongLike() {
        return new RuleSet(Pairs.KOREAN, true, true);
    }

    public Pairs getPairsType() {
        return pairsType;
    }

    public boolean isForceShowPairs() {
        return forceShowPairs;
    }

    public boolean isOfferToAll() {
        return offerToAll;
    }

    private static Set<Domino> militaries(int p1, int p2) {
        return Set.of(Domino.of(p1), Domino.of(p2));
    }

}
