package io.navpil.github.zenzen.rummy.kapshap;

public class KapShapRuleset {

    private final Offer offerType;
    private final boolean takeAfterOffer;
    private final int setCount;
    private final int winningHandSize;
    private final boolean continueAfterDominoesEnd;

    public enum Offer {
        ALL, LAST, NONE
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
