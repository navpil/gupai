package io.github.navpil.gupai.rummy.kapshap;

public class KapShapRuleset {

    private final Offer offerType;
    /**
     * KapShap is special in that player not only picks the discarded tile, but also picks the closed tile after it.
     * This is more reminiscent of fishing games, which have this mechanics, but not so usual for the rummy-like games.
     *
     * This fixes the problem that the latest discarded tile is not accessible to the next players, but then too many
     * dominoes are accessible.
     *
     * Even though by default KapShap has this as true, this should be configurable.
     */
    private final boolean takeAfterOffer;

    /**
     * Number of GuPai sets used. For 2 players that's 1, but for KapTaiShap that can be a pretty large number.
     */
    private final int setCount;

    /**
     * This is 8 for KapShap and 10 for KapTaiShap. Has to be more than 2 and even number for KapShap-like game.
     */
    private final int winningHandSize;

    /**
     * Technically game can continue when
     */
    private final boolean continueAfterDominoesEnd;

    public enum Offer {
        /**
         * Offer any opened domino to the player.
         * This is a very special KapShap variation, other games do not have it
         */
        ALL,

        /**
         * Offer only last discarded domino to the player.
         * This is usual for the other rummy-like games, but then the problem is that if only the next player is
         * offered a tile, then other players who could wait for the tile to complete their hand will miss it.
         */
        LAST,

        /**
         * No dominoes are offered, discarded dominoes are removed from the game forever.
         * This is a setting for test purposes, since no game actualy have this requirement
         */
        NONE
    }

    public static KapShapRuleset culinKapTaiShap(int setCount) {
        return new KapShapRuleset(Offer.ALL, true, setCount, 10, false);
    }

    public static KapShapRuleset culinKapShap() {
        return new KapShapRuleset(Offer.ALL, true, 1, 8, false);
    }

    public static KapShapRuleset classicKapTaiShap(int setCount) {
        return new KapShapRuleset(Offer.LAST, true, setCount, 10, false);
    }

    public static KapShapRuleset classicKapShap() {
        return new KapShapRuleset(Offer.LAST, true, 1, 8, false);
    }

    public KapShapRuleset(Offer offerType, boolean takeAfterOffer, int setCount, int winningHandSize, boolean continueAfterDominoesEnd) {
        this.offerType = offerType;
        this.takeAfterOffer = takeAfterOffer;
        this.setCount = setCount;
        this.winningHandSize = winningHandSize;
        this.continueAfterDominoesEnd = continueAfterDominoesEnd;
    }


    public Offer getOfferType() {
        return offerType;
    }

    public boolean isTakeAfterOffer() {
        return takeAfterOffer;
    }

    public int getSetCount() {
        return setCount;
    }

    public int getWinningHandSize() {
        return winningHandSize;
    }

    public boolean isContinueAfterDominoesEnd() {
        return continueAfterDominoesEnd;
    }
}
