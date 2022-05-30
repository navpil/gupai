package io.github.navpil.gupai.mod10.daling;

import io.github.navpil.gupai.mod10.Mod10Rule;

import java.util.List;

public class RuleSet {

    /**
     * Whether banker will win all the stakes when no one can match his points (exact point game only)
     */
    private final boolean letBankerWinNonMatches;
    /**
     * How much banker will lose in exactPoint game when there is an absolute winner (exact point game only)
     */
    private final int bankerLossOnAbsoluteWinner;


    public RuleSet(boolean letBankerWinNonMatches, int bankerLossOnAbsoluteWinner) {
        this.letBankerWinNonMatches = letBankerWinNonMatches;
        this.bankerLossOnAbsoluteWinner = bankerLossOnAbsoluteWinner;
    }

    /**
     * My ruleset with a banker winning pushes and losing to an absolute winner
     *
     * @return adjusted Macao rule set
     */
    public static RuleSet macaoWithBanker() {
        return new RuleSet(true, 5);
    }

    /**
     * Probably the most logical rule set, where banker wins or loses nothing, but simply provides the tiles.
     * In case of several players winning - there is a push.
     *
     * @return ruleset as described in the Macao book
     */
    public static RuleSet macao() {
        return new RuleSet(false, 0);
    }

    public DaLingBet getFixedBet() {
        return new DaLingBet(List.of(3, 7, 15));
    }

    public Mod10Rule getBankersPairCalculation() {
        return Mod10Rule.DA_LING_BANKER;
    }

    public boolean isLetBankerWinNonMatches() {
        return letBankerWinNonMatches;
    }

    public int getBankerLossOnAbsoluteWinner() {
        return bankerLossOnAbsoluteWinner;
    }
}
