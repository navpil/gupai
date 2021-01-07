package io.github.navpil.gupai.jielong;

import io.github.navpil.gupai.jielong.player.MutableInteger;
import io.github.navpil.gupai.ChineseDominoSet;
import io.github.navpil.gupai.dominos.Domino;
import io.github.navpil.gupai.jielong.player.evaluators.CombiningMoveEvaluator;
import io.github.navpil.gupai.jielong.player.evaluators.MinMaxMoveEvaluator;
import io.github.navpil.gupai.jielong.player.Player;
import io.github.navpil.gupai.jielong.player.PlayerFactory;
import io.github.navpil.gupai.jielong.player.PriorityPlayer;
import io.github.navpil.gupai.jielong.player.evaluators.RarenessMoveEvaluator;
import io.github.navpil.gupai.jielong.player.RealPlayer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class JieLongSimulation {

    public static void main(String[] args) {

        final List<String> names = List.of("Random", "MinMax", "Rare", "PipCounter");

        List<Player> players = new ArrayList<>();

        final boolean realPlayerGame = false;
        if (realPlayerGame) {
            players.add(new RealPlayer(names.get(0)));
        } else {
            players.add(PlayerFactory.createRandomPlayerImpl(names.get(0)));
        }

        players.add(new PriorityPlayer(names.get(1), new CombiningMoveEvaluator().addEvaluator(new RarenessMoveEvaluator())));
        players.add(new PriorityPlayer(names.get(2), new CombiningMoveEvaluator().addEvaluator(new MinMaxMoveEvaluator())));
        players.add(new PriorityPlayer(names.get(3), new CombiningMoveEvaluator().addEvaluator(new RarenessMoveEvaluator())));

        final int simCount = 1;
        int whoGoesFirst = 0;
        final List<Domino> set = ChineseDominoSet.create();

        runSeveralSimulations(players, simCount, whoGoesFirst, set, new JieLongRuleSet());
    }

    /**
     * This allows to run various simulations, except for DingNiu, which is too different to try to generalize it here
     *
     * @param players list of players
     * @param simCount number of simulations to run
     * @param whoGoesFirst who has the initial move (usually 0)
     * @param dominoSet domino set to use
     * @param ruleSet who to calculate the points after each game
     */
    public static void runSeveralSimulations(
            List<Player> players,
            int simCount,
            int whoGoesFirst,
            List<Domino> dominoSet,
            RuleSet ruleSet) {
        Map<String, Integer> indexes = new HashMap<>();
        final List<String> names = players.stream().map(Player::getName).collect(Collectors.toList());
        for (int i = 0; i < names.size(); i++) {
            indexes.put(names.get(i), i);
        }
        Map<String, Integer> runningTotal = new LinkedHashMap<>();
        for (String name : names) {
            runningTotal.put(name, 0);
        }

        Map<String, MutableInteger> whoWentFirst = new HashMap<>();
        for (String name : names) {
            whoWentFirst.put(name, new MutableInteger());
        }

        final int tilesPerPlayer = dominoSet.size() / players.size();

        for (int i = 0; i < simCount; i++) {
            Collections.shuffle(dominoSet);
            final ArrayList<Domino> shuffledSet = new ArrayList<>(dominoSet);

            for (int counter = 0; counter < players.size(); counter++) {
                players.get(counter).deal(shuffledSet.subList(counter * tilesPerPlayer, (counter + 1) * tilesPerPlayer));
            }
            final Stats stats;
            if (ruleSet.continueTheGame(players)) {
                whoWentFirst.get(names.get(whoGoesFirst)).add(1);
                stats = runSimulation(new Dragon(Dragon.OpenArms.SINGLE, dominoSet, new NoopSuanZhang()), players, tilesPerPlayer, whoGoesFirst);
            } else {
                stats = null;
            }

            final RuleSet.WinningStats winningStats = ruleSet.resolvePoints(stats, players, whoGoesFirst);

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
        for (Map.Entry<String, MutableInteger> total : whoWentFirst.entrySet()) {
            System.out.println("Player " + total.getKey() + " got " + total.getValue().getCount() + " first moves");
        }
    }

    public static Stats runSimulation(Dragon dragon, List<Player> hands, int tilesPerPlayer, int whoGoesFirst) {
        hands.forEach(System.out::println);

        final Move firstMove = hands.get(whoGoesFirst).firstMove();
        dragon.executeMove(firstMove);

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
            stats.put(name, points);
        }
        return stats;
    }

}
