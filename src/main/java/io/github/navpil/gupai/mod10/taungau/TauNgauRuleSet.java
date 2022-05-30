package io.github.navpil.gupai.mod10.taungau;

import io.github.navpil.gupai.mod10.Mod10Rule;

public class TauNgauRuleSet {

    private final Mod10Rule mod10Rule;
    private final Calculation calculation;
    private final int fixedStake;

    private final static int NONE = -1;

    public TauNgauRuleSet(Mod10Rule mod10Rule, Calculation calculation, int fixedStake) {
        this.mod10Rule = mod10Rule;
        this.calculation = calculation;
        this.fixedStake = fixedStake;
    }

    public static TauNgauRuleSet pagat() {
        return new TauNgauRuleSet(Mod10Rule.TAU_NGAU, new PagatCalculation(), NONE);
    }

    public static TauNgauRuleSet macao() {
        return new TauNgauRuleSet(Mod10Rule.DA_LING, new MacaoCalculation(4, 4), 1);
    }

    public static TauNgauRuleSet macaoFixed() {
        return new TauNgauRuleSet(Mod10Rule.DA_LING, new MacaoCalculation(NONE, 4), 1);
    }

    public Mod10Rule getMod10Rule() {
        return mod10Rule;
    }

    public Calculation getCalculation() {
        return calculation;
    }

    public int getFixedStake() {
        return fixedStake;
    }

    @FunctionalInterface
    public interface Calculation {

        int calculatePlayerWinning(boolean playerDiscarded, boolean bankerDiscarded, int playerPoints, int bankerPoints);
    }

    private static class PagatCalculation implements Calculation {

        @Override
        public int calculatePlayerWinning(boolean playerDiscarded, boolean bankerDiscarded, int playerPoints, int bankerPoints) {
            if (playerDiscarded && bankerDiscarded) {
                return Integer.compare(playerPoints, bankerPoints);
            } else if (playerDiscarded) {
                return 1;
            } else if (bankerDiscarded) {
                return -1;
            }
            return 0;
        }
    }

    private static class MacaoCalculation implements Calculation {

        private final int fixedLostMultiplier;
        private final int lostPlayerPaysNoMatterWhat;

        public MacaoCalculation(int fixedLostMultiplier, int lostPlayerPaysNoMatterWhat) {
            this.fixedLostMultiplier = fixedLostMultiplier;
            this.lostPlayerPaysNoMatterWhat = lostPlayerPaysNoMatterWhat;
        }

        @Override
        public int calculatePlayerWinning(boolean playerDiscarded, boolean bankerDiscarded, int playerPoints, int bankerPoints) {
            if (playerDiscarded && bankerDiscarded) {
                return playerPoints - bankerPoints;
            } else if (playerDiscarded) {
                return playerPoints;
            } else if (bankerDiscarded) {
                return fixedLostMultiplier > 0 ? -(fixedLostMultiplier) : -bankerPoints;
            }
            return -lostPlayerPaysNoMatterWhat;
        }
    }


}
