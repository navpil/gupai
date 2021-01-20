package io.github.navpil.gupai.mod10.daling;

import io.github.navpil.gupai.Domino;
import io.github.navpil.gupai.DominoUtil;

import java.util.List;
import java.util.Map;

public class Table {

    public enum GameColor {
        RED, BLACK
    }

    private final RuleSet ruleSet;
    private final List<Domino> bankersPair;
    private final Map<String, DaLingBet> bets;

    public Table(RuleSet ruleSet, List<Domino> bankersPair, Map<String, DaLingBet> bets) {
        this.ruleSet = ruleSet;
        this.bankersPair = bankersPair;
        this.bets = bets;
    }

    /**
     * If banker's pair has less than 4 red dots the game is black, otherwise it's red
     *
     * @return Game color (red or black)
     */
    public GameColor getColor() {
        final int redPointsCount = bankersPair.stream().mapToInt(DominoUtil::redPointsCount).sum();
        return redPointsCount < 4 ? GameColor.BLACK : GameColor.RED;
    }

    public int getBankerPoints() {
        return ruleSet.getBankersPairCalculation().getPoints(bankersPair).getMax();
    }

    public RuleSet getRuleSet() {
        return ruleSet;
    }

    public Map<String, DaLingBet> getBets() {
        return bets;
    }
}
