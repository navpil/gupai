package io.github.navpil.gupai.tricks.shiwuhu;

public class RuleSet {

    public enum WinningCondition {
        //Taking 15 tricks is a win
        TRICKS,
        //Getting rid of 15 cards is a win
        GET_RID,
        //Same as GET_RID, but in order to win at least one trick (15th or any next one) should be taken
        GET_RID_ON_LAST_TRICK,
        //No winning condition - all cards are played out - testing only
        NONE;
    }

    /**
     * When a player has won a trick, but he hasn't reached 15 Hu (possible only in TRICK winning condition) - what to
     * do next? There are two possible outcomes - stop the game with no one won (this setting is set to true) or to
     * continue the game with the next player with cards leading a card to a trick (set to false).
     */
    private final boolean breakOnEmptyLead;
    private final WinningCondition winningCondition;

    public RuleSet(boolean breakOnEmptyLead, WinningCondition winningCondition) {
        this.breakOnEmptyLead = breakOnEmptyLead;
        this.winningCondition = winningCondition;
    }

    public boolean isBreakOnEmptyLead() {
        return breakOnEmptyLead;
    }

    public WinningCondition getWinningCondition() {
        return winningCondition;
    }
}
