package io.github.navpil.gupai.mod10.paigow;

import io.github.navpil.gupai.Domino;
import io.github.navpil.gupai.mod10.Mod10Rule;
import io.github.navpil.gupai.XuanHePuPai;

import java.util.HashMap;
import java.util.Map;

public enum PaiGowHandRanking {

    /**
     * This is a case when [4:2] ranks higher than fives and [2:1] ranks lower than fives
     */
    SUPREME_SIX_HIGH(6, 3),

    /**
     * Practically these two are equivalent, there is no point in distinguishing these
     */
    SUPREME_BOTH_LOW(3, 3),
    SUPREME_BOTH_HIGH( 6, 6);

    private final Map<Domino, Integer> RANKING = new HashMap<>();

    {
        RANKING.put(Domino.of(66), 23);
        RANKING.put(Domino.of(11), 22);
        RANKING.put(Domino.of(44), 21);
        RANKING.put(Domino.of(31), 20);

        RANKING.put(Domino.of(55), 17);
        RANKING.put(Domino.of(33), 16);
        RANKING.put(Domino.of(22), 15);

        RANKING.put(Domino.of(65), 13);
        RANKING.put(Domino.of(64), 12);
        RANKING.put(Domino.of(61), 11);
        RANKING.put(Domino.of(51), 10);

        RANKING.put(Domino.of(63), 9);
        RANKING.put(Domino.of(54), 9);
        RANKING.put(Domino.of(62), 8);
        RANKING.put(Domino.of(53), 8);
        RANKING.put(Domino.of(52), 7);
        RANKING.put(Domino.of(43), 7);
        RANKING.put(Domino.of(41), 5);
        RANKING.put(Domino.of(32), 5);

//        RANKING.put(Domino.of(21), ?);
//        RANKING.put(Domino.of(42), ?);
    }

    PaiGowHandRanking(int sixValue, int threeValue) {
        RANKING.put(Domino.of(42), sixValue);
        RANKING.put(Domino.of(21), threeValue);
    }

    /**
     * Compares dominoes - which one is higher.
     *
     * @param o1 first domino
     * @param o2 second domino
     * @return -1 if first is lower (dominoes came in natural order), 1 if first is higher
     */
    public int compareDominoes(Domino o1, Domino o2) {
        return RANKING.get(o1).compareTo(RANKING.get(o2));
    }

    /**
     * Compares pairs - which one is higher.
     *
     * @param o1 first pair
     * @param o2 second pair
     * @return -1 if first is lower (pairs came in natural order), 1 if first is higher
     */
    public int comparePairs(PaiGowPair o1, PaiGowPair o2) {
        int rank1 = getOveralRanking(o1);
        int rank2 = getOveralRanking(o2);

        if (rank1 > rank2) {
            return 1;
        } else if (rank1 < rank2) {
            return -1;
        }
        if (rank1 == 0) {
            return 0;
        }
        return compareDominoes(normalizedPair(o1).get(0), normalizedPair(o2).get(0));
    }

    /**
     * Returns an arbitrarily chosen pair ranking - with Supreme ranking highest (36) and mod10[0] ranking the lowest (0).
     * Does not resolve ties.
     *
     * @param pair pair to rank
     * @return ranking
     */
    public int getOveralRanking(PaiGowPair pair) {
        final XuanHePuPai.Combination combination = XuanHePuPai.xiangShiFu().evaluate(pair.getDominos());
        if (combination == XuanHePuPai.Combination.SUPREME_PAIR) {
            return 36;
        }
        if (combination == XuanHePuPai.Combination.CIVIL_PAIR || combination == XuanHePuPai.Combination.MILITARY_CHINESE_PAIR) {
            return 10 + RANKING.get(pair.get(0));
        }
        if (isWong(pair)) {
            return 12;
        }
        if (isKong(pair)) {
            return 11;
        }
        return Mod10Rule.TAU_NGAU.getPoints(pair.getDominos()).getMax();
    }

    /**
     * Describes a PaiGow pair in a human understandable format.
     * Returns one of: supreme, pair[x], wong, kong, mod10[x].
     *
     * @param pair pair to describe
     * @return human readable description
     */
    public String describe(PaiGowPair pair) {
        return describe(getOveralRanking(pair));
    }

    private String describe(int i) {
        if (i == 36) {
            return "supreme";
        }
        if (i > 12) {
            return "pair[" + (i - 10) + "]";
        }
        if (i == 12) {
            return "wong";
        }
        if (i == 11) {
            return "kong";
        }
        return "mod10[" + i + "]";
    }

    private boolean isWong(PaiGowPair pair) {
        return Domino.ofList(66, 11).contains(pair.get(0)) && Mod10Rule.TAU_NGAU.getPoints(pair.get(1)).getMax() == 9;
    }

    private boolean isKong(PaiGowPair pair) {
        return Domino.ofList(66, 11).contains(pair.get(0)) && Mod10Rule.TAU_NGAU.getPoints(pair.get(1)).getMax() == 8;
    }

    private PaiGowPair normalizedPair(PaiGowPair pair) {
        if (compareDominoes(pair.get(0), pair.get(1)) > 0) {
            return pair;
        }
        return new PaiGowPair(pair.get(1), pair.get(0));
    }

    /**
     * Compares hands - which one is higher.
     *
     * If dealer should have an advantage on zeros, then banker's hand has to be passed as a second parameter
     *
     * @param hand first hand
     * @param banker second hand
     * @return Hand comparison, which may be resolved differently.
     */
    public HandComparison compareHands(Hand hand, Hand banker) {
        Hand normalHand = normalizedHand(hand);
        Hand normalBankers = normalizedHand(banker);
        return new HandComparison(comparePairs(normalHand.getFirst(), normalBankers.getFirst()), comparePairs(normalHand.getSecond(), normalBankers.getSecond()));
    }

    private Hand normalizedHand(Hand hand) {
        if (comparePairs(hand.getFirst(), hand.getSecond()) > 0) {
            return hand;
        }
        return new Hand(hand.getSecond(), hand.getFirst());
    }

    /**
     * Full normalization means that Hand will have higher pair coming first and both pairs will have higher domino coming first
     *
     * @param hand hand to normalize
     * @return normalized hand
     */
    public Hand fullyNormalize(Hand hand) {
        return normalizedHand(new Hand(normalizedPair(hand.getFirst()), normalizedPair(hand.getSecond())));
    }

    /**
     * Hand comparison can result differently, depending on how 0 are handled.
     */
    public static class HandComparison {

        private final int high;
        private final int low;

        public HandComparison(int high, int low) {
            this.high = high;
            this.low = low;
        }

        /**
         * Simplest resolution when a banker has no advantage
         *
         * @return simple hand comparison
         */
        public int resolve() {
            return high + low;
        }

        /**
         * Resolution when a banker has advantage
         *
         * @return banker's at advantage hand comparison
         */
        public int resolveZeroesLow() {
            return zeroLow(high) + zeroLow(low);
        }

        private int zeroLow(int maybeZero) {
            return maybeZero == 0 ? -1 : maybeZero;
        }

    }
}
