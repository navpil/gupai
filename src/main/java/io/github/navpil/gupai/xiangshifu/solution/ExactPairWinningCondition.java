package io.github.navpil.gupai.xiangshifu.solution;

import io.github.navpil.gupai.CombinationType;
import io.github.navpil.gupai.xiangshifu.Move;
import io.github.navpil.gupai.xiangshifu.Triplet;

import java.util.List;

/**
 * In order to win a pair in the middle should be exactly as a specified
 */
public class ExactPairWinningCondition implements WinningCondition {

    private final CombinationType expectedPair;

    public ExactPairWinningCondition(CombinationType expectedPair) {
        this.expectedPair = expectedPair;
    }

    @Override
    public boolean hasWon(List<Triplet> triplets) {
        for (Triplet triplet : triplets) {
            final CombinationType combination = triplet.getCombination();
            if (combination == CombinationType.none) {
                return false;
            }
            if (combination.isPair() && !(combination == expectedPair)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public int compareMoves(Move m1, Move m2) {
        int pairBreakage = 0;
        if (breaksPair(m1)) {
            pairBreakage += 10;
        }
        if (breaksPair(m2)) {
            pairBreakage -= 10;
        }
        if (createsPair(m1)) {
            pairBreakage -= 9;
        }
        if (createsPair(m2)) {
            pairBreakage += 9;
        }
        if (brekesPairPotential(m1)) {
            pairBreakage += 8;
        }
        if (brekesPairPotential(m2)) {
            pairBreakage -= 8;
        }
        if (createsPairPotential(m1)) {
            pairBreakage -= 8;
        }
        if (createsPairPotential(m2)) {
            pairBreakage += 8;
        }
        if (pairBreakage != 0) {
            return pairBreakage;
        }
        return m1.compareTo(m2);
    }

    private boolean createsPair(Move move) {
        return checkPair(move.getFutureCombination1(), move.getFutureCombination2());
    }

    private boolean createsPairPotential(Move move) {
        return checkPair(move.getFuturePairPotential1(), move.getFuturePairPotential2());
    }

    private boolean breaksPair(Move move) {
        return checkPair(move.getCombination1(), move.getCombination2());
    }

    private boolean brekesPairPotential(Move move) {
        return checkPair(move.getPairPotential1(), move.getPairPotential2());
    }

    private boolean checkPair(CombinationType first, CombinationType second) {
        return expectedPair.equals(first) || expectedPair.equals(second);
    }
}
