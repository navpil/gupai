package io.github.navpil.gupai.tricks.tianjiu;

public class RuleSet {

    public enum TrickType {
        //Single cards only
        GU_PAI,

        //Pairs allowed, but no mixed
        TIAN_JIU_NO_MIXING,

        //Mixed pairs are also allowed
        TIAN_JIU_ONLY_PAIRS,

        //Mixed large tricks are allowed
        TIAN_JIU;
    }

    /**
     * Various rules allow different trick types, for example KuPai allows only single tile tricks,
     * ancient TianJiu only allows pairs
     */
    private final TrickType trickType;
    /**
     * Some sources mention and videos show that [1:1][2:2][3:3] and [4:4][5:5][6:6] are valid tricks
     */
    private final boolean allow123;
    /**
     * This is only a variation, but pretty popular one - that in order to take the last single-tile trick,
     * at least some tricks should have been taken before. This rule does not apply to the last non-single-tile trick
     */
    private final boolean earlyDeath;
    /**
     * Mostly the discarded tiles are turned face down, but KuPai rules say that all tiles should be exposed, as in
     * most trick-taking European games.
     */
    private final boolean discardedCardExposed;
    /**
     * Usually banker has a x2 multiplier, some variations increase it by 1 for each time players is a banker in a row
     */
    private final boolean bankersMultiplierIncreased;

    public static RuleSet classicTianJiu() {
        return new RuleSet(TrickType.TIAN_JIU, false, true, false, true);
    }

    public static RuleSet kuPai() {
        return new RuleSet(TrickType.GU_PAI, false, false, true, false);
    }

    public RuleSet(TrickType trickType, boolean allow123, boolean earlyDeath, boolean discardedCardExposed, boolean bankersMultiplierIncreased) {
        this.allow123 = allow123;
        this.trickType = trickType;
        this.earlyDeath = earlyDeath;
        this.discardedCardExposed = discardedCardExposed;
        this.bankersMultiplierIncreased = bankersMultiplierIncreased;
    }

    public TrickType getTrickType() {
        return trickType;
    }

    public boolean isAllow123() {
        return allow123;
    }

    public boolean isEarlyDeath() {
        return earlyDeath;
    }

    public boolean isDiscardedCardExposed() {
        return discardedCardExposed;
    }

    public boolean isBankersMultiplierIncreased() {
        return bankersMultiplierIncreased;
    }
}
