package io.github.navpil.gupai.rummy.jjakmatchugi;

import io.github.navpil.gupai.Domino;
import io.github.navpil.gupai.Pairs;

import java.util.Set;

public class RuleSet {

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
