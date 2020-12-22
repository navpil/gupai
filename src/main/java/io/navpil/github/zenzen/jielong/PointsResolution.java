package io.navpil.github.zenzen.jielong;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

public interface PointsResolution {

    WinningStats resolvePoints(Stats stats, List<String> originalNameList, int whoGoesFirst);

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
