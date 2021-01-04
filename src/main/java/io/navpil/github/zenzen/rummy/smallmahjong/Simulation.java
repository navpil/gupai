package io.navpil.github.zenzen.rummy.smallmahjong;

import io.navpil.github.zenzen.dominos.Domino;
import io.navpil.github.zenzen.jielong.Stats;
import io.navpil.github.zenzen.util.CombineCollection;

import java.util.Collection;
import java.util.List;

public class Simulation {

    public static Stats runSimulation(List<Domino> dominos, List<Player> players, int whoGoesFirst) {

        int currentPlayer = whoGoesFirst;
        int cardsPerPlayer = 5;

        final Table table = new Table();
        for (int i = 0; i < players.size(); i++) {
            final Player player = players.get(i);
            final List<Domino> deal = dominos.subList(i * cardsPerPlayer, (i + 1) * cardsPerPlayer);
            System.out.println("Player " + player.getName() + " was dealt " + deal);
            player.deal(deal);
            player.showTable(table);

        }
        int currentDominoIndex = players.size() * cardsPerPlayer;

        while (currentDominoIndex < dominos.size()) {
            Player player = players.get(currentPlayer);
            boolean tookDiscard = false;
            if (table.lastDiscard() != null) {
                int playerWhoTakesTheDiscard = currentPlayer;

                //Since this is mahjong style game, so other players can also take the tile


                boolean onlyWinAllowed = false;
                //First offer to the current player, if he wants it
                Triplet triplet = player.offer(table.lastDiscard(), TripletType.STRAIGHTS);
                if (triplet == null) {
                    //First player refused the domino, try the second one
                    playerWhoTakesTheDiscard = (playerWhoTakesTheDiscard + 1) % players.size();
                    final Player secondPlayer = players.get(playerWhoTakesTheDiscard);
                    triplet = secondPlayer.offer(table.lastDiscard(), TripletType.MIXED);
                    if (triplet == null) {
                        //Second player refuzed the domino
                        if (players.size() > 3) {
                            //Try the third one, if he exists:
                            playerWhoTakesTheDiscard = (playerWhoTakesTheDiscard + 1) % players.size();
                            final Player thirdPlayer = players.get(playerWhoTakesTheDiscard);
                            triplet = thirdPlayer.offer(table.lastDiscard(), TripletType.SETS);
                            if (triplet != null) {
                                //Third player accepted domino, he is on the move
                                currentPlayer = playerWhoTakesTheDiscard;
                                player = thirdPlayer;
                                final TripletType tripletType = HandCalculator.evaluate(triplet);
                                if (tripletType == null) {
                                    throw new IllegalStateException("Cannot have an invalid triplet");
                                } else if (tripletType != TripletType.SETS) {
                                    onlyWinAllowed = true;
                                }
                            }
                        }
                    } else {
                        //Second player accepted domino, he is on the move
                        final TripletType tripletType = HandCalculator.evaluate(triplet);
                        if (tripletType == null) {
                            throw new IllegalStateException("Cannot have an invalid triplet");
                        } else if (tripletType == TripletType.STRAIGHTS) {
                            onlyWinAllowed = true;
                        }
                        currentPlayer = playerWhoTakesTheDiscard;
                        player = secondPlayer;
                    }
                } else {
                    //First player decided to take a discard
                    if (HandCalculator.evaluate(triplet) == null) {
                        throw new IllegalStateException("Cannot have an invalid triplet");
                    }
                }


                if (triplet != null) {
                    tookDiscard = true;
                    table.add(player.getName(), triplet);
                    System.out.println(player.getName() + " chose to take " + table.lastDiscard() + " to form " + triplet);
                    table.remove(table.lastDiscard());

                    if (player.hasWon()) {
                        final Hand winningHand = player.getWinningHand();
                        final Collection<Triplet> tableTriplets = table.getTriplets(player.getName());
                        int points = HandCalculator.calculatePoints(player.getWinningHand(), tableTriplets, tookDiscard);
                        System.out.println(player.getName() + " won with combinations " + new CombineCollection<>(List.of(winningHand.triplets, tableTriplets)));
                        return statsFor(player.getName(), points);
                    } else if (onlyWinAllowed) {
                        throw new IllegalStateException("Player could only take a last discarded for winning");
                    }
                    final Domino discard = player.getDiscard();
                    table.add(discard);
                    System.out.println(player.getName() + " discarded " + discard);
                }
            }
            if (!tookDiscard) {
                final Domino give = dominos.get(currentDominoIndex);
                currentDominoIndex++;
                System.out.println(player.getName() + " was given " + give);
                player.give(give);

                if (player.hasWon()) {
                    final Collection<Triplet> tableTriplets = table.getTriplets(player.getName());
                    System.out.println(player.getName() + " won with combinations " + new CombineCollection<>(List.of(player.getWinningHand().getTriplets(), tableTriplets)));
                    int points = HandCalculator.calculatePoints(player.getWinningHand(), tableTriplets, tookDiscard);
                    return statsFor(player.getName(), points);
                }
                final Domino discard = player.getDiscard();
                table.add(discard);
                System.out.println(player.getName() + " discarded " + discard);
            }
            currentPlayer = (currentPlayer + 1) % players.size();
        }
        System.out.println("No one won");
        return new Stats();
    }

    private static Stats statsFor(String name, int points) {
        final Stats stats = new Stats();
        stats.put(name, points);
        return stats;
    }

}
