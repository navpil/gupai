package io.github.navpil.gupai.jielong;

import io.github.navpil.gupai.jielong.player.Player;
import io.github.navpil.gupai.util.Stats;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Interface to describe how to resolve points given Stats
 */
public interface RuleSet {

    WinningStats resolvePoints(Stats stats, List<? extends Player> players, int whoGoesFirst);

    default boolean continueTheGame(List<? extends Player> players) {
        return true;
    }

    /**
     * Resolved points together with the winner's name
     */
    class WinningStats {
        private LinkedHashMap<String, Integer> points;
        private String winner;

        public WinningStats(HashMap<String, Integer> points, String winner) {
            this.points = new LinkedHashMap<>(points);
            this.winner = winner;
        }

        public HashMap<String, Integer> getPoints() {
            return points;
        }

        public String getWinner() {
            return winner;
        }

        @Override
        public String toString() {
            return "WinningStats{" +
                    "points=" + points +
                    ", winner='" + winner + '\'' +
                    '}';
        }
    }

}
