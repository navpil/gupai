package io.github.navpil.gupai.shiwuhu;

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
