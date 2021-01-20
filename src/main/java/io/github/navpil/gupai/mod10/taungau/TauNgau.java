package io.github.navpil.gupai.mod10.taungau;

import io.github.navpil.gupai.ChineseDominoSet;
import io.github.navpil.gupai.Domino;
import io.github.navpil.gupai.mod10.Mod10Rule;
import io.github.navpil.gupai.mod10.RunGamblingGame;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class TauNgau {

    public static final Mod10Rule MOD_10_RULE = Mod10Rule.TAU_NGAU;

    public static void main(String[] args) {
        final List<Domino> deck = ChineseDominoSet.create();
        final List<Player> gamblers = List.of(
                new BankerPlayer("Banker"),
                new ComputerPlayer("Comp-2", 100),
                new RealPlayer("Jim", 100)
        );
        new RunGamblingGame().runManyGames(deck, gamblers, 100, null, true, (dominos, players, ruleSet, banker) -> runSimulation(dominos, players, banker));
    }

    public static RunGamblingGame.RunResult runSimulation(List<Domino> dominos, List<Player> players, int bankerIndex) {

        final HashMap<String, Integer> bets = new HashMap<>();
        final HashMap<String, Boolean> discards = new HashMap<>();

        //Bets
        for (int i = 0; i < players.size(); i++) {
            if (i == bankerIndex) {
                continue;
            }
            final Player player = players.get(i);
            bets.put(player.getName(), player.placeBet());
        }
        //Deal
        for (int i = 0; i < players.size(); i++) {
            players.get(i).deal(dominos.subList(i * 5, (i + 1) * 5));
        }
        //Discard
        for (final Player player : players) {
            Collection<Domino> discard = player.discard();
            System.out.println(player.getName() + " discarded " + (discard.isEmpty() ? "nothing" : discard));
            discards.put(player.getName(), !discard.isEmpty() && isValid(discard));
        }

        //Calculation
        final Player banker = players.get(bankerIndex);
        boolean bankerDiscarded = discards.get(banker.getName());
        int bankerPoints = bankerDiscarded ? MOD_10_RULE.getPoints(banker.hand()).getMax() : -1;
        if (bankerDiscarded) {
            System.out.println("Banker has a hand of " + banker.hand());
        } else {
            System.out.println("Banker did not discard");
        }
        for (int i = 0; i < players.size(); i++) {
            if (i == bankerIndex) {
                continue;
            }
            final Player player = players.get(i);
            final boolean playerDiscarded = discards.get(player.getName());
            final Integer stake = bets.get(player.getName());
            if (bankerDiscarded && playerDiscarded) {
                final Integer playerPoints = MOD_10_RULE.getPoints(player.hand()).getMax();
                System.out.println(player. getName() + " got " + player.hand());
                if (playerPoints > bankerPoints) {
                    player.win(stake);
                    banker.lose(stake);
                    System.out.println(player.getName() + " won");
                } else if (bankerPoints > playerPoints) {
                    player.lose(stake);
                    banker.win(stake);
                    System.out.println(player.getName() + " lost");
                } else {
                    System.out.println(player.getName() + " pushed");
                }

            } else {
                if (bankerDiscarded) {
                    System.out.println(player.getName() + " did not discard, lost");
                    player.lose(stake);
                    banker.win(stake);
                } else if (playerDiscarded) {
                    System.out.println(player.getName() + " discarded, won");
                    player.win(stake);
                    banker.lose(stake);
                } else {
                    System.out.println(player.getName() + " did not discard, push");
                }
            }
        }
        System.out.println(players.stream().map(p -> p.getName() + ": " + p.getMoney()).collect(Collectors.toList()));
        if (bankerDiscarded) {
            return RunGamblingGame.RunResult.CHANGE_BANKER;
        } else {
            return RunGamblingGame.RunResult.KEEP_BANKER;
        }

    }

    private static boolean isValid(Collection<Domino> discard) {
        if (discard.size() != 3) {
            return false;
        }
        return Mod10Rule.TAU_NGAU.getPoints(discard).isMod10();
    }


}
