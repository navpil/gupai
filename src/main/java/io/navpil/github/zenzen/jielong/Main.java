package io.navpil.github.zenzen.jielong;

import io.navpil.github.zenzen.ChineseDominoSet;
import io.navpil.github.zenzen.dominos.Domino;
import io.navpil.github.zenzen.jielong.player.CombiningStrategyPlayerImpl;
import io.navpil.github.zenzen.jielong.player.Player;
import io.navpil.github.zenzen.jielong.player.RarenessPlayerImpl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class Main {

    public static void main(String[] args) {
        long randomTotal = 0;
        long normalTotal = 0;
        final int total = 100;
        for (int i = 0; i < total; i++) {
            final Stats stats = runDingNiu();
            int normals = 0;
            for (Map.Entry<String, Integer> stringIntegerEntry : stats.points.entrySet()) {
                final String key = stringIntegerEntry.getKey();
                final Integer points = stringIntegerEntry.getValue();
                if ("Random".equals(key)) {
                    randomTotal += points;
//                    randomAverage = ((randomAverage * i) + stringIntegerEntry.getValue()) /  (i + 1);
                } else {
                    normalTotal += points;
//                    normals += stringIntegerEntry.getValue();
                }
            }
//            normalAverage += ((normalAverage * i) + normals) / (i + 1);
        }
//        System.out.println("Randoim avg: " + randomAverage + ", normal : " + normalAverage);
        System.out.println("R: " + (randomTotal * 1.0 / total) + " N: " + (normalTotal * 1.0 / (total * 3)));
    }

    private static void runJieLong() {
        final List<Domino> dominos = ChineseDominoSet.create();
        Collections.shuffle(dominos);

        runTheGame(dominos, Dragon.OpenArms.SINGLE);
    }

    private static Stats runDingNiu() {
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
        Collections.shuffle(dominos);

        return runTheGame(dominos, Dragon.OpenArms.DOUBLE);
    }

    private static Stats runTheGame(List<Domino> dominos, Dragon.OpenArms dragonType) {
        final ArrayList<Player> hands = new ArrayList<>();
        final int players = 4;
        final int perPlayer = dominos.size() / players;

        final int realPlayerHand = new Random().nextInt(4);

        for (int i = 0; i < players; i++) {
            final List<Domino> sublist = dominos.subList(i * perPlayer, (i + 1) * perPlayer);
            if (i == realPlayerHand) {
                hands.add(new CombiningStrategyPlayerImpl("Random", sublist));
            } else {
                hands.add(new RarenessPlayerImpl("Hand" + i, sublist));
            }
        }
//        for (int i = 0; i < players; i++) {
//            final List<RealDomino> sublist = realDominos.subList(i * perPlayer, (i + 1) * perPlayer);
//            if (i == realPlayerHand) {
//                hands.add(new RealPlayerHand("Dima", sublist));
//            } else {
//                hands.add(new MinMaxHandImpl("Hand" + i, sublist));
//            }
//        }


        //Number of moves is exactly number of dominoes per player
        return runSimulation(dragonType, hands, perPlayer);

    }

    private static Stats runSimulation(Dragon.OpenArms dragonType, ArrayList<Player> hands, int perPlayer) {
        final Dragon dragon = new Dragon(hands.get(0).firstMove(), dragonType);
        System.out.println(dragon);
        int downCounter = 0;
        int players = hands.size();
        game_loop:
        for (int i = 1; i < players * perPlayer; i++) {
            int playerNo = i % players;
            final Player player = hands.get(playerNo);
            final Move move = player.extractMove(dragon);
            System.out.print("Player " + player.getName() + " ");
            if (move.getSide() < 0) {
                System.out.println("put a domino down: " + move.getDomino());
                downCounter++;
                if (downCounter == 4) {
                    System.out.println("Four passes in a row, game over");
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
