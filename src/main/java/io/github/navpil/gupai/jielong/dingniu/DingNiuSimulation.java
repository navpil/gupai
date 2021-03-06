package io.github.navpil.gupai.jielong.dingniu;

import io.github.navpil.gupai.ChineseDominoSet;
import io.github.navpil.gupai.Domino;
import io.github.navpil.gupai.jielong.Dragon;
import io.github.navpil.gupai.jielong.Move;
import io.github.navpil.gupai.jielong.player.Player;
import io.github.navpil.gupai.jielong.player.PlayerFactory;
import io.github.navpil.gupai.jielong.player.PriorityPlayer;
import io.github.navpil.gupai.jielong.player.RealPlayer;
import io.github.navpil.gupai.jielong.player.evaluators.CombiningMoveEvaluator;
import io.github.navpil.gupai.jielong.player.evaluators.MinMaxMoveEvaluator;
import io.github.navpil.gupai.jielong.player.evaluators.RarenessMoveEvaluator;
import io.github.navpil.gupai.jielong.player.evaluators.SuanZhangMoveEvaluator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Because of SuanZhang the DingNiu simulation is different to the JieLong, that's why it's written separately.
 */
public class DingNiuSimulation {

    public static void main(String[] args) {

        //Use Integers instead of names everywhere

        final List<String> names = List.of("Dima", "MinMax", "Rare", "Combine");

        List<Player> players = new ArrayList<>();

        final boolean realPlayerGame = false;
        if (realPlayerGame) {
            players.add(new RealPlayer(names.get(0)));
        } else {
            players.add(PlayerFactory.createCombiningStrategyPlayer(names.get(0)));
        }

        players.add(new PriorityPlayer(names.get(1), new CombiningMoveEvaluator().addEvaluator(new MinMaxMoveEvaluator()).addEvaluator(new SuanZhangMoveEvaluator(), 0)));
        players.add(new PriorityPlayer(names.get(2), new CombiningMoveEvaluator().addEvaluator(new RarenessMoveEvaluator()).addEvaluator(new SuanZhangMoveEvaluator(), 0)));
        players.add(new PriorityPlayer(names.get(3), new CombiningMoveEvaluator().addEvaluator(new MinMaxMoveEvaluator()).addEvaluator(new RarenessMoveEvaluator()).addEvaluator(new SuanZhangMoveEvaluator(), 0)));


        Map<String, Integer> indexes = new HashMap<>();//Map.of(names.get(0), 0, "MinMax", 1, "Rare", 2, "Combine", 3);
        for (int i = 0; i < names.size(); i++) {
            indexes.put(names.get(i), i);
        }

        final int simCount = 100;
        int whoGoesFirst = 0;
        final List<Domino> set = ChineseDominoSet.dingNiuSet();

        Map<String, Integer> runningTotal = new LinkedHashMap<>();
        for (String name : names) {
            runningTotal.put(name, 0);
        }

        final int tilesPerPlayer = set.size() / players.size();

        for (int i = 0; i < simCount; i++) {
            Collections.shuffle(set);
            final ArrayList<Domino> shuffledSet = new ArrayList<>(set);

            for (int counter = 0; counter < players.size(); counter++) {
                players.get(counter).deal(shuffledSet.subList(counter * tilesPerPlayer, (counter + 1) * tilesPerPlayer));
            }

            final DingNiuStats stats = runSimulation(players, tilesPerPlayer, whoGoesFirst);
            final WinningStats winningStats = resolvePoints(stats, players.stream().map(Player::getName).collect(Collectors.toList()), whoGoesFirst);

            whoGoesFirst = indexes.get(winningStats.winner);
            System.out.println("Next lead for " + whoGoesFirst);
            for (Map.Entry<String, Integer> entry : winningStats.points.entrySet()) {
                final String name = entry.getKey();
                final Integer oldValue = runningTotal.get(name);
                runningTotal.put(name, oldValue + entry.getValue());
            }
            System.out.println("Game score: " + winningStats);
            System.out.println("Cumulative score: " + runningTotal);
            if (stats.getSuanZhangPlayer() >= 0) {
                System.out.println("Player who SuanZhang-ed: " + stats.getSuanZhangPlayer());
            } else if (stats.getGameBlocked()) {
                System.out.println("Game was blocked");
            }
        }

        for (Map.Entry<String, Integer> total : runningTotal.entrySet()) {
            System.out.println("Player " + total.getKey() + " got " + total.getValue() + " points");
        }
    }

    public static WinningStats resolvePoints(DingNiuStats stats, List<String> originalNameList, int whoGoesFirst) {
        Map<String, Integer> order = new HashMap<>();
        List<String> names = new ArrayList<>(originalNameList);
        for (int i = 0, size = names.size(); i < size; i++) {
            int playerIndex = (whoGoesFirst + i) % size;
            order.put(names.get(playerIndex), i);
        }

        names.sort((n1, n2) -> {
            final Integer p1 = stats.getPointsFor(n1);
            final Integer p2 = stats.getPointsFor(n2);
            if (!p1.equals(p2)) {
                return p1.compareTo(p2);
            }
            return order.get(n1).compareTo(order.get(n2));
        });
        final String winner = names.get(0);
        names.remove(winner);

        //Losers have a negative preference (in a reverse order)
        names.sort((n1, n2) -> {
            final Integer p1 = stats.getPointsFor(n1);
            final Integer p2 = stats.getPointsFor(n2);
            if (!p1.equals(p2)) {
                return p1.compareTo(p2);
            }
            return order.get(n2).compareTo(order.get(n1));
        });

        names.add(0, winner);
        int suanZhangMultiplier = 1;
        if (stats.getSuanZhangPlayer() >= 0) {
            final String suanZhangName = originalNameList.get(stats.getSuanZhangPlayer());
            if (suanZhangName.equals(names.get(0))) {
                suanZhangMultiplier = 2;
            } else {
                final HashMap<String, Integer> points = new HashMap<>();
                for (String name : names) {
                    points.put(name, 0);
                }
                points.put(suanZhangName, -12);
                points.put(winner, 12);
                return new WinningStats(points, winner);
            }
        }

        final HashMap<String, Integer> points = new HashMap<>();
        int counter = 0;
        for (String name : names) {
            switch (counter) {
                case 0: points.put(name, 6 * suanZhangMultiplier);break;
                case 1: points.put(name, -1 * suanZhangMultiplier);break;
                case 2: points.put(name, -2 * suanZhangMultiplier);break;
                case 3: points.put(name, -3 * suanZhangMultiplier);break;
            }
            counter++;
        }
        return new WinningStats(points, winner);
    }

    public static DingNiuStats runSimulation(List<Player> hands, int tilesPerPlayer, int whoGoesFirst) {
        hands.forEach(System.out::println);

        final Move firstMove = hands.get(whoGoesFirst).firstMove();
        final Dragon dragon = Dragon.dingNiuDragon();
        dragon.executeMove(firstMove);

        System.out.println(dragon);
        int downCounter = 0;
        int players = hands.size();
        //We add 1 anyway on the first iteration
        int playerNo = whoGoesFirst;
        int suanZhangPlayer = -1;
        boolean isGameBlocked = false;
        //Number of moves is exactly number of dominoes per player
        game_loop:
        for (int i = 1; i < players * tilesPerPlayer; i++) {
            playerNo++;
            playerNo = playerNo % players;
            final Player player = hands.get(playerNo);
            final Move move = player.extractMove(dragon);
            System.out.print("Player " + player.getName() + " ");
            if (move.getSide() < 0) {
                System.out.println("put a domino down: " + move.getDomino());
                downCounter++;
                if (downCounter == players) {
                    System.out.println(downCounter + " passes in a row, game over");
                    break game_loop;
                }
            } else {
                downCounter = 0;
                System.out.println("played " + move);
            }
            boolean isSuanZhangMove = move.isSuanZhang();

            dragon.executeMove(move);
            System.out.println(dragon);

            //If the move is suanZhang

            final SuanZhang.Type type = dragon.suanZhang().suanZhangType();
            if (isSuanZhangMove) {
                System.out.println("SuanZhang! " + type);
                if (suanZhangPlayer >= 0 ) {
                    throw new IllegalStateException("Cannot have two SuanZhangs!");
                }
                suanZhangPlayer = playerNo;
                //If CLASSIC, then continue playing
                if (type == SuanZhang.Type.SMOTHERED) {
                    System.out.println("Ending the game with immediate SuanZhang");
                    break game_loop;
                }
            } else if (type != SuanZhang.Type.NONE) {
                isGameBlocked = true;
                System.out.println("---------- Looks like SuanZhang, but it's NOT -------- " + type);
                if (type == SuanZhang.Type.SMOTHERED) {
                    System.out.println("Ending game immediately");
                    break game_loop;
                }
            }
        }

        final DingNiuStats stats = new DingNiuStats();
        for (Player player : hands) {
            final String name = player.getName();
            final int points = player.getPoints();
            System.out.println(name + " has points: " + points);
            stats.put(name, points);
        }
        //Only SuanZhang if poitns are more than 0, otherwise - no SuanZhang counted
        if (suanZhangPlayer >= 0 && hands.get(suanZhangPlayer).getPoints() > 0) {
            stats.setSuanZhangPlayer(suanZhangPlayer);
        }
        stats.setGameBlocked(isGameBlocked);
        return stats;
    }

    public static class WinningStats {
        private LinkedHashMap<String, Integer> points;
        private String winner;

        public WinningStats(HashMap<String, Integer> points, String winner) {
            this.points = new LinkedHashMap<>(points);
            this.winner = winner;
        }

        public HashMap<String, Integer> getPoints() {
            return points;
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
