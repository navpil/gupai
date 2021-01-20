package io.github.navpil.gupai.mod10.paigow;

import io.github.navpil.gupai.ChineseDominoSet;
import io.github.navpil.gupai.Domino;
import io.github.navpil.gupai.util.PlayersGroup;
import io.github.navpil.gupai.mod10.RunGamblingGame;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class PaiGow {

    /**
     * There are three types of ranking, but two of them are the same in practice.
     */
    private final PaiGowHandRanking ranking;

    /**
     * Many sources say that in case of a tie (say [5:5][3:2] vs [5:5][4:1]) it's a dealer's (banker's) win.
     *
     * This should be configurable.
     */
    private final boolean dealerAdvantage;

    /**
     * Maximum allowed stake amount
     */
    private final int maxStake;

    /**
     * Small pai gow is usually played in a family setting with a single pair dealt, so no calculation involved
     */
    private final boolean smallPaiGow;

    public PaiGow(PaiGowHandRanking handRanking, boolean dealerAdvantage, int maxStake, boolean smallPaiGow) {
        ranking = handRanking;
        this.dealerAdvantage = dealerAdvantage;
        this.maxStake = maxStake;
        this.smallPaiGow = smallPaiGow;
    }

    public static PaiGow casinoPaiGow() {
        return new PaiGow(PaiGowHandRanking.SUPREME_BOTH_LOW, true, 10, false);
    }

    public static PaiGow familyPaiGow() {
        return new PaiGow(PaiGowHandRanking.SUPREME_BOTH_LOW, false, 10, true);
    }

    public static void main(String[] args) {
        boolean casinoPaiGow = false;

        final PaiGow paiGow;
        if (casinoPaiGow) {
            paiGow = PaiGow.casinoPaiGow();
        } else {
            paiGow = PaiGow.familyPaiGow();
        }
        final PaiGowHandRanking ranking = paiGow.ranking;
        System.out.println(new RunGamblingGame().runManyGames(ChineseDominoSet.create(), List.of(
                new ComputerPlayer("Casino", 100, ranking),
                new ComputerPlayer("Casino-2", 100, ranking),
                new RandomPlayer("Human", 100)
        ), 1000, casinoPaiGow, paiGow::runSimulation).stream().map(p -> p.getName() + " has " + p.getMoney() + " left").collect(Collectors.joining("\n")));
    }

    public RunGamblingGame.RunResult runSimulation(List<Domino> dominos, List<Player> allPlayers, int bankerIndex) {
        final PlayersGroup<Player> playersGroup = new PlayersGroup<>(allPlayers, bankerIndex);
        final Deck deck = new Deck(dominos);

        Map<String, Integer> stakes = new LinkedHashMap<>();

        for (Player player : playersGroup.getPlayers()) {
            final Integer stake = player.stake(maxStake);
            stakes.put(player.getName(), stake);
        }

        if (smallPaiGow) {
            PaiGowPair bankersPair = new PaiGowPair(deck.take(2));
            final Player banker = playersGroup.getBanker();
            System.out.println("Banker got " + bankersPair + " ( " + ranking.describe(bankersPair) + " ) ");
            for (Player player : playersGroup.getPlayers()) {
                final PaiGowPair playersPair = new PaiGowPair(deck.take(2));
                System.out.println(player.getName() + " got " + playersPair + " ( " + ranking.describe(playersPair) + " ) ");

                int comparison = ranking.comparePairs(playersPair, bankersPair);
                if (dealerAdvantage && comparison == 0) {
                    comparison = -1;
                }

                int stake = stakes.get(player.getName());
                if (comparison > 0) {
                    System.out.println(player.getName() + " won");
                    player.win(stake);
                    banker.lose(stake);
                } else if (comparison < 0) {
                    System.out.println(player.getName() + " lost");
                    player.lose(stake);
                    banker.win(stake);
                } else {
                    System.out.println("PUSH");
                }
            }
        } else {
            for (Player player : playersGroup.getPlayers()) {
                player.deal(deck.take(4));
            }

            final Player banker = playersGroup.getBanker();
            banker.deal(deck.take(4));

            Hand bankerHand = ranking.fullyNormalize(banker.hand());

            for (Player player : playersGroup.getPlayers()) {
                final Hand hand = ranking.fullyNormalize(player.hand());
                final PaiGowHandRanking.HandComparison handComparison = ranking.compareHands(hand, bankerHand);
                final int i = dealerAdvantage ? handComparison.resolveZeroesLow() : handComparison.resolve();

                System.out.println(player.getName() + ": " + hand +
                        "( " + (ranking.describe(hand.getFirst())) + ", " + (ranking.describe(hand.getSecond())) + " )"
                        + " vs bankers: " + bankerHand +
                        "( " + (ranking.describe(bankerHand.getFirst())) + ", " + (ranking.describe(bankerHand.getSecond())) + " )"

                );
                int stake = stakes.get(player.getName());
                if (i > 0) {
                    System.out.println(player.getName() + " won");
                    player.win(stake);
                    banker.lose(stake);
                } else if (i < 0) {
                    System.out.println(player.getName() + " lost");
                    player.lose(stake);
                    banker.win(stake);
                } else {
                    System.out.println("PUSH");
                }
            }
        }
        return RunGamblingGame.RunResult.CHANGE_BANKER;
    }


}
