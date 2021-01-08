package io.github.navpil.gupai.shiwuhu.tianjiu;

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

    private final TrickType trickType;
    private final boolean allow123;
    private final boolean earlyDeath;

    public static RuleSet classicTianJiu() {
        return new RuleSet(TrickType.TIAN_JIU, false, true);
    }

    public RuleSet(TrickType trickType, boolean allow123, boolean earlyDeath) {
        this.allow123 = allow123;
        this.trickType = trickType;
        this.earlyDeath = earlyDeath;
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
}
