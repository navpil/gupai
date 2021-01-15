package io.github.navpil.gupai.mod10.daling;

import io.github.navpil.gupai.mod10.Mod10Rule;

import java.util.List;

public class RuleSet {
    /**
     * Whether the bet is fixed to some predefined value and players cannot choose it
     */
    private final DaLingBet fixedBet;
    /**
     * How to calculate bankers pair - should the [2:1] and [4:2] be wild, or count 3 and 6 respectively
     */
    private final Mod10Rule bankersPairCalculation;

    /**
     * whether players have to match the banker's points exactly and not overrank it
     */
    private final boolean exactPointGame;
    /**
     * Whether banker will win all the stakes when no one can match his points (exact point game only)
     */
    private final boolean letBankerWinNonMatches;
    /**
     * How much banker will lose in exactPoint game when there is an absolute winner (exact point game only)
     */
    private final int bankerLossOnAbsoluteWinner;


    public RuleSet(DaLingBet fixedBet, Mod10Rule bankersPairCalculation, boolean exactPointGame, boolean letBankerWinNonMatches, int bankerLossOnAbsoluteWinner) {
        this.fixedBet = fixedBet;
        this.bankersPairCalculation = bankersPairCalculation;
        this.exactPointGame = exactPointGame;
        this.letBankerWinNonMatches = letBankerWinNonMatches;
        this.bankerLossOnAbsoluteWinner = bankerLossOnAbsoluteWinner;
    }

    public static RuleSet macaoWithBanker() {
        return new RuleSet(new DaLingBet(List.of(3, 7, 15)), Mod10Rule.DA_LING_BANKER, true, true, 5);
    }

    public static RuleSet macao() {
        return new RuleSet(new DaLingBet(List.of(3, 7, 15)), Mod10Rule.DA_LING_BANKER, true, false, 0);
    }

    public static RuleSet alternative() {
        return new RuleSet(null, Mod10Rule.DA_LING, false, false, 0);
    }

    public DaLingBet getFixedBet() {
        return fixedBet;
    }

    public Mod10Rule getBankersPairCalculation() {
        return bankersPairCalculation;
    }

    public boolean isExactPointGame() {
        return exactPointGame;
    }

    public boolean isLetBankerWinNonMatches() {
        return letBankerWinNonMatches;
    }

    public int getBankerLossOnAbsoluteWinner() {
        return bankerLossOnAbsoluteWinner;
    }
}
