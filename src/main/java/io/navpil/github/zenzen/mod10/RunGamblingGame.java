package io.navpil.github.zenzen.mod10;

import io.navpil.github.zenzen.ChineseDominoSet;
import io.navpil.github.zenzen.dominos.Domino;
import io.navpil.github.zenzen.fishing.tsungshap.NamedPlayer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class RunGamblingGame {

    public interface Gambler extends NamedPlayer {

        int getMoney();

        default boolean stillHasMoney() {
            //Should always return true for the CasinoBanker
            return getMoney() > 0;
        }

        default boolean isBankrupt() {
            return !stillHasMoney();
        }
    }

    enum ChangeBanker {
        TRUE, FALSE
    }

    public interface SimulationFunction<X, Y> {
        ChangeBanker runSimulation(List<Domino> dominos, List<X> players, Y ruleSet, int banker);
    }

    public  <T extends Gambler, RuleSet> List<T> runManyGames(List<Domino> deck, List<T> gamblers, int maxSimCount, RuleSet ruleSet, boolean casinoGame, SimulationFunction<T, RuleSet> simFunction) {
        List<T> players = new ArrayList<>(gamblers);
        int sim = maxSimCount;
        int banker = 0;
        game_loop:
        while (players.size() > 1 && sim-- > 0) {
            if (!casinoGame) {
                System.out.println(players.get(banker).getName() + " is a Banker");
            }
            Collections.shuffle(deck);

            final ChangeBanker changeBanker = simFunction.runSimulation(deck, players, ruleSet, banker);

            if (!casinoGame) {
                System.out.println(players.get(banker).getName() + " got " + players.get(banker).getMoney());
            }

            if (casinoGame) {
                final ArrayList<T> newGamblers = new ArrayList<>();
                newGamblers.add(players.get(banker));
                newGamblers.addAll(players.subList(1, players.size()).stream().filter(Gambler::stillHasMoney).collect(Collectors.toList()));
                players = newGamblers;
                if (players.get(banker).isBankrupt()) {
                    //This should not happen, but if it happens - here it goes, we stop the game
                    break game_loop;
                }
            } else {
                int nextBanker = changeBanker == ChangeBanker.TRUE ? banker + 1 : banker;
                for (int i = 0; i <= banker; i++) {
                    if (players.get(i).isBankrupt()) {
                        nextBanker--;
                    }
                }

                players = players.stream().filter(Gambler::stillHasMoney).collect(Collectors.toList());
                banker = (nextBanker) % players.size();
            }
        }

        if (casinoGame) {
            final List<T> won = new ArrayList<>();
            for (int i = 0; i < players.size(); i++) {
                if (i == banker) continue;
                won.add(players.get(i));
            }
            return won;
        } else {
            return players;
        }

    }

}
