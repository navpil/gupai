package io.navpil.github.zenzen.jielong;

import io.navpil.github.zenzen.ChineseDominoSet;
import io.navpil.github.zenzen.dominos.Domino;
import io.navpil.github.zenzen.jielong.player.CombiningStrategyPlayerImpl;
import io.navpil.github.zenzen.jielong.player.MinMaxPlayerImpl;
import io.navpil.github.zenzen.jielong.player.Player;
import io.navpil.github.zenzen.jielong.player.RarenessPlayerImpl;
import io.navpil.github.zenzen.jielong.player.RealPlayer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class DingNiuSimulation {

    public static void main(String[] args) {

        final List<String> names = List.of("Dima", "MinMax", "Rare", "Combine");

        List<Function<List<Domino>, Player>> playerFactories = new ArrayList<>();

        playerFactories.add(list -> new RealPlayer(names.get(0), list));
        playerFactories.add(list -> new MinMaxPlayerImpl(names.get(1), list));
        playerFactories.add(list -> new RarenessPlayerImpl(names.get(2), list));
        playerFactories.add(list -> new CombiningStrategyPlayerImpl(names.get(3), list));

        Map<String, Integer> indexes = new HashMap<>();//Map.of(names.get(0), 0, "MinMax", 1, "Rare", 2, "Combine", 3);
        for (int i = 0; i < names.size(); i++) {
            indexes.put(names.get(i), i);
        }

        final int simCount = 1000;
        int whoGoesFirst = 2;
        final List<Domino> set = getDingNiuShuffledSet();

        Map<String, Integer> runningTotal = new HashMap<>();
        for (String name : names) {
            runningTotal.put(name, 0);
        }

        final int tilesPerPlayer = set.size() / playerFactories.size();

        for (int i = 0; i < simCount; i++) {
            Collections.shuffle(set);
            final ArrayList<Domino> shuffledSet = new ArrayList<>(set);

            final ArrayList<Player> hands = new ArrayList<>();

            for (int counter = 0; counter < playerFactories.size(); counter++) {
                final Function<List<Domino>, Player> playerFactory = playerFactories.get(counter);
                hands.add(playerFactory.apply(shuffledSet.subList(counter * tilesPerPlayer, (counter + 1) * tilesPerPlayer)));
            }

            final Stats stats = runSimulation(Dragon.OpenArms.DOUBLE, hands, tilesPerPlayer, whoGoesFirst);
            final WinningStats winningStats = resolvePoints(stats, hands.stream().map(Player::getName).collect(Collectors.toList()), whoGoesFirst);

            whoGoesFirst = indexes.get(winningStats.winner);
            System.out.println("Next lead for " + whoGoesFirst);
            for (Map.Entry<String, Integer> entry : winningStats.points.entrySet()) {
                final String name = entry.getKey();
                final Integer oldValue = runningTotal.get(name);
                runningTotal.put(name, oldValue + entry.getValue());
            }
            System.out.println("Cumulative score: " + runningTotal);
        }

        for (Map.Entry<String, Integer> total : runningTotal.entrySet()) {
            System.out.println("Player " + total.getKey() + " got " + total.getValue() + " points");
        }
    }

    public static WinningStats resolvePoints(Stats stats, List<String> names, int whoGoesFirst) {
        Map<String, Integer> order = new HashMap<>();
        for (int i = 0, size = names.size(); i < size; i++) {
            int playerIndex = (whoGoesFirst + i) % size;
            order.put(names.get(playerIndex), i);
        }

        names.sort((n1, n2) -> {
            final Integer p1 = stats.points.get(n1);
            final Integer p2 = stats.points.get(n2);
            if (!p1.equals(p2)) {
                return p1.compareTo(p2);
            }
            return order.get(n1).compareTo(order.get(n2));
        });
        final String winner = names.get(0);
        names.remove(winner);

        //Losers have a negative preference (in a reverse order)
        names.sort((n1, n2) -> {
            final Integer p1 = stats.points.get(n1);
            final Integer p2 = stats.points.get(n2);
            if (!p1.equals(p2)) {
                return p1.compareTo(p2);
            }
            return order.get(n2).compareTo(order.get(n1));
        });

        names.add(0, winner);

        final HashMap<String, Integer> points = new HashMap<>();
        int counter = 0;
        for (String name : names) {
            switch (counter) {
                case 0: points.put(name, 6);break;
                case 1: points.put(name, -1);break;
                case 2: points.put(name, -2);break;
                case 3: points.put(name, -3);break;
            }
            counter++;
        }
        return new WinningStats(points, winner);
    }

    private static Stats runSimulation(Dragon.OpenArms dragonType, ArrayList<Player> hands, int tilesPerPlayer, int whoGoesFirst) {

        final Dragon dragon = new Dragon(hands.get(whoGoesFirst).firstMove(), dragonType);

        System.out.println(dragon);
        int downCounter = 0;
        int players = hands.size();
        //We add 1 anyway on the first iteration
        int playerNo = whoGoesFirst;
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
            dragon.executeMove(move);
            System.out.println(dragon);
        }

        final Stats stats = new Stats();
        for (Player player : hands) {
            final String name = player.getName();
            final int points = player.getPoints();
            System.out.println(name + " has points: " + points);
            stats.add(name, points);
        }
        return stats;
    }

    private static List<Domino> getDingNiuShuffledSet() {
        final List<Domino> dominos = ChineseDominoSet.create();
        dominos.remove(new Domino(4,5));//One nine
        dominos.remove(new Domino(3,5));//One eight

        //Two sevens
        dominos.remove(new Domino(4,3));
        dominos.remove(new Domino(2,5));

        //Six
        dominos.remove(new Domino(4,2));

        //Two fives
        dominos.remove(new Domino(4,1));
        dominos.remove(new Domino(3,2));

        //Three
        dominos.remove(new Domino(2,1));
        return dominos;
    }

    public static class WinningStats {
        private HashMap<String, Integer> points;
        private String winner;

        public WinningStats(HashMap<String, Integer> points, String winner) {
            this.points = points;
            this.winner = winner;
        }

        public HashMap<String, Integer> getPoints() {
            return points;
        }
    }
}
