package io.github.navpil.gupai.mod10.kolyesi;

import io.github.navpil.gupai.DominoUtil;
import io.github.navpil.gupai.ChineseDominoSet;
import io.github.navpil.gupai.Domino;
import io.github.navpil.gupai.mod10.Mod10Rule;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

public class KolYeSi {

    public static void main(String[] args) {

        int sim = 100;
        int minStake = 1;
        int maxStake = 10;
        final int money = 100;

        List<KolYeSiPlayer> players = new ArrayList<>(List.of(
                new ComputerKolYeSiPlayer("Jim", money, 5, 0),
                new ComputerKolYeSiPlayer("Jack", money, 9, 0),
                new FixedKolYeSiPlayer("Fixed 1", money, 5, 1),
                new FixedKolYeSiPlayer("Fixed 2", money, 5, 2),
                new RealKolYeSiPlayer("Panteleymon", money),
                new RandomKolYeSiPlayer("Jannie", money)
        ));

        runManySimulations(sim, minStake, maxStake, players, 0, false);

    }

    private static void runManySimulations(int sim, int minStake, int maxStake, List<KolYeSiPlayer> players, int banker, boolean casinoGame) {
        if (casinoGame) {
            players.add(0, new KolYeSiBanker());
            banker = 0;
        }
        final List<Domino> dominos = ChineseDominoSet.create();
        int[] stakes = new int[players.size()];
        List<List<Domino>> dealtDominoes = new ArrayList<>();
        for (int i = 0; i < players.size(); i++) {
            dealtDominoes.add(new ArrayList<>());
        }

        while (players.size() > 1 && sim-- > 0) {
            if (!casinoGame) {
                System.out.println(players.get(banker).getName() + " is a Banker");
            }
            Collections.shuffle(dominos);
            for (List<Domino> dealtDominoe : dealtDominoes) {
                dealtDominoe.clear();
            }
            final Iterator<Domino> it = dominos.iterator();

            for (int i = 0; i < players.size(); i++) {
                if (i == banker) continue;
                final Domino dealtDomino = it.next();
                dealtDominoes.get(i).add(dealtDomino);
                stakes[i] = players.get(i).stake(dealtDomino, minStake, maxStake);
            }
            for (int i = 0; i < players.size(); i++) {
                if (i == banker) continue;
                System.out.println("Player " + players.get(i).getName() + " staked " + stakes[i]);
            }

            final Domino d1 = it.next();
            final Domino d2 = it.next();

            System.out.println("Banker got " + d1 + ", " + d2);

            if (DominoUtil.isWenPair(d1, d2)) {
                System.out.println("Perfect pair, instant banker win");
                for (int i = 0; i < players.size(); i++) {
                    if (i == banker) continue;
                    final KolYeSiPlayer player = players.get(i);
                    player.lose(stakes[i]);
                    players.get(banker).win(stakes[i]);
                    System.out.println(player + " left: " + player.getMoney());
                }
            } else {
                for (int i = 0; i < players.size(); i++) {
                    if (i == banker) continue;
                    final int count = players.get(i).dominoCount(d1, d2);
                    final List<Domino> userDominoes = dealtDominoes.get(i);
                    userDominoes.add(it.next());
                    if (count == 2) {
                        userDominoes.add(it.next());
                    }
                }
                int bankersPoints = Mod10Rule.KOL_YE_SI.getPoints(List.of(d1, d2)).getMax();

                for (int i = 0; i < players.size(); i++) {
                    if (i == banker) continue;
                    final KolYeSiPlayer player = players.get(i);
                    final String name = player.getName();
                    final int stake = stakes[i];
                    final List<Domino> dealt = dealtDominoes.get(i);

                    int playerPoints = Mod10Rule.KOL_YE_SI.getPoints(dealt).getMax();
                    String message;
                    if (bankersPoints == playerPoints) {
                        message = "PUSH";
                    } else if (bankersPoints > playerPoints) {
                        message = "LOSS";
                        player.lose(stake);
                        players.get(banker).win(stake);
                    } else {
                        message = "WIN";
                        player.win(stake);
                        players.get(banker).lose(stake);
                    }
                    System.out.println(name + " vs Banker : " + playerPoints + " vs " + bankersPoints + " ( " + dealtDominoes.get(i) + " ) " + " : " + message + " left: " + player.getMoney());
                }
            }

            if (!casinoGame) {
                System.out.println(players.get(banker).getName() + " got " + players.get(banker).getMoney());
            }

            if (casinoGame) {
                players = players.stream().filter(KolYeSiPlayer::stillHasMoney).collect(Collectors.toList());
            } else {
                int nextBanker = banker + 1;
                for (int i = 0; i <= banker; i++) {
                    if (players.get(i).isBankrupt()) {
                        nextBanker--;
                    }
                }

                players = players.stream().filter(KolYeSiPlayer::stillHasMoney).collect(Collectors.toList());
                banker = (nextBanker) % players.size();
            }
        }

        if (casinoGame) {
            final ArrayList<KolYeSiPlayer> won = new ArrayList<>();
            for (int i = 0; i < players.size(); i++) {
                if (i == banker) continue;
                won.add(players.get(i));
            }
            showGameWon(won);
        } else {
            showGameWon(players);
        }
    }

    private static void showGameWon(List<KolYeSiPlayer> won) {
        final List<String> wonPlayers = won.stream().map(KolYeSiPlayer::getName).collect(Collectors.toList());
        if (wonPlayers.isEmpty()) {
            System.out.println("All players lost");
        } else {
            System.out.println("Game won by " + wonPlayers);
        }
    }

}
