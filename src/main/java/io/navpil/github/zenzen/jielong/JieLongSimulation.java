package io.navpil.github.zenzen.jielong;

import io.navpil.github.zenzen.ChineseDominoSet;
import io.navpil.github.zenzen.dominos.Domino;
import io.navpil.github.zenzen.jielong.player.evaluators.CombiningMoveEvaluator;
import io.navpil.github.zenzen.jielong.player.Counter;
import io.navpil.github.zenzen.jielong.player.evaluators.MinMaxMoveEvaluator;
import io.navpil.github.zenzen.jielong.player.Player;
import io.navpil.github.zenzen.jielong.player.PlayerFactory;
import io.navpil.github.zenzen.jielong.player.PriorityPlayer;
import io.navpil.github.zenzen.jielong.player.evaluators.RarenessMoveEvaluator;
import io.navpil.github.zenzen.jielong.player.RealPlayer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class JieLongSimulation {

    public static void main(String[] args) {

        final List<String> names = List.of("Random", "MinMax", "Rare", "PipCounter");

        List<Function<List<Domino>, Player>> playerFactories = new ArrayList<>();

        final boolean realPlayerGame = false;
        if (realPlayerGame) {
            playerFactories.add(list -> new RealPlayer(names.get(0), list));
        } else {
            playerFactories.add(list -> PlayerFactory.createRandomPlayerImpl(names.get(0), list));
        }

        playerFactories.add(list -> new PriorityPlayer(names.get(1), list, new CombiningMoveEvaluator().addEvaluator(new RarenessMoveEvaluator())));
        playerFactories.add(list -> new PriorityPlayer(names.get(2), list, new CombiningMoveEvaluator().addEvaluator(new MinMaxMoveEvaluator())));
        playerFactories.add(list -> new PriorityPlayer(names.get(3), list, new CombiningMoveEvaluator().addEvaluator(new RarenessMoveEvaluator())));

        final int simCount = 1000;
        int whoGoesFirst = 0;
        final List<Domino> set = ChineseDominoSet.create();

        runSeveralSimulations(names, playerFactories, simCount, whoGoesFirst, set, new JieLongPointsResolution());
    }

    /**
     * This allows to run various simulations, except for DingNiu, which is too different to try to generalize it here
     *
     * @param names list of user names
     * @param playerFactories list of factories to create players
     * @param simCount number of simulations to run
     * @param whoGoesFirst who has the initial move (usually 0)
     * @param dominoSet domino set to use
     * @param pointsResolution who to calculate the points after each game
     */
    public static void runSeveralSimulations(List<String> names, List<Function<List<Domino>, Player>> playerFactories, int simCount, int whoGoesFirst, List<Domino> dominoSet, PointsResolution pointsResolution) {
        Map<String, Integer> indexes = new HashMap<>();//Map.of(names.get(0), 0, "MinMax", 1, "Rare", 2, "Combine", 3);
        for (int i = 0; i < names.size(); i++) {
            indexes.put(names.get(i), i);
        }
        Map<String, Integer> runningTotal = new LinkedHashMap<>();
        for (String name : names) {
            runningTotal.put(name, 0);
        }

        Map<String, Counter> whoWentFirst = new HashMap<>();
        for (String name : names) {
            whoWentFirst.put(name, new Counter());
        }

        final int tilesPerPlayer = dominoSet.size() / playerFactories.size();

        for (int i = 0; i < simCount; i++) {
            Collections.shuffle(dominoSet);
            final ArrayList<Domino> shuffledSet = new ArrayList<>(dominoSet);

            final ArrayList<Player> hands = new ArrayList<>();

            for (int counter = 0; counter < playerFactories.size(); counter++) {
                final Function<List<Domino>, Player> playerFactory = playerFactories.get(counter);
                hands.add(playerFactory.apply(shuffledSet.subList(counter * tilesPerPlayer, (counter + 1) * tilesPerPlayer)));
            }
            whoWentFirst.get(names.get(whoGoesFirst)).add(1);
            final Stats stats = runSimulation(Dragon.OpenArms.SINGLE, hands, tilesPerPlayer, whoGoesFirst);
            final PointsResolution.WinningStats winningStats = pointsResolution.resolvePoints(stats, hands.stream().map(Player::getName).collect(Collectors.toList()), whoGoesFirst);

            whoGoesFirst = indexes.get(winningStats.getWinner());
            System.out.println("Next lead for " + whoGoesFirst);
            for (Map.Entry<String, Integer> entry : winningStats.getPoints().entrySet()) {
                final String name = entry.getKey();
                final Integer oldValue = runningTotal.get(name);
                runningTotal.put(name, oldValue + entry.getValue());
            }
            System.out.println("Game score: " + winningStats);
            System.out.println("Cumulative score: " + runningTotal);
        }

        for (Map.Entry<String, Integer> total : runningTotal.entrySet()) {
            System.out.println("Player " + total.getKey() + " got " + total.getValue() + " points");
        }
        for (Map.Entry<String, Counter> total : whoWentFirst.entrySet()) {
            System.out.println("Player " + total.getKey() + " got " + total.getValue().getCount() + " first moves");
        }
    }

    public static Stats runSimulation(Dragon.OpenArms dragonType, List<Player> hands, int tilesPerPlayer, int whoGoesFirst) {
        hands.forEach(System.out::println);

        final Move firstMove = hands.get(whoGoesFirst).firstMove();
        final Dragon dragon = new Dragon(firstMove, dragonType, new SuanZhangImpl());

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

}
