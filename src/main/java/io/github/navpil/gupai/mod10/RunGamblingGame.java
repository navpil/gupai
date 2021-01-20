package io.github.navpil.gupai.mod10;

import io.github.navpil.gupai.fishing.tsungshap.NamedPlayer;
import io.github.navpil.gupai.Domino;

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

    public enum RunResult {
        CHANGE_BANKER, KEEP_BANKER, STOP_THE_GAME;
    }

    public interface SimulationFunctionWithRuleSet<X, Y> {
        RunResult runSimulation(List<Domino> dominos, List<X> players, Y ruleSet, int banker);
    }

    public interface SimulationFunction<X> {
        RunResult runSimulation(List<Domino> dominos, List<X> players, int banker);
    }

    /**
     * Allows to run several gambling games in a row
     *
     * @param deck deck to use (usually chinese deck of 32 dominoes)
     * @param gamblers gamblers are players and a banker (if he is dedicated)
     * @param maxSimCount maximum number of simulations to run
     * @param casinoGame if set to true, game is CASINO and banker does not change, if set to false, game is FRIENDLY with a round robin banker
     * @param simFunction function to run
     * @param <T> player type
     * @return players which are still in the game
     */
    public  <T extends Gambler> List<T> runManyGames(List<Domino> deck, List<T> gamblers, int maxSimCount, boolean casinoGame, SimulationFunction<T> simFunction) {
        return runManyGames(deck, gamblers, maxSimCount, null, casinoGame, (dominos, players, ruleSet, banker) -> simFunction.runSimulation(dominos, players, banker));
    }

    /**
     * Allows to run several gambling games in a row
     *
     * @param deck deck to use (usually chinese deck of 32 dominoes)
     * @param gamblers gamblers are players and a banker (if he is dedicated)
     * @param maxSimCount maximum number of simulations to run
     * @param ruleSet rule set is simply passed to the simulation function, can be anything
     * @param casinoGame if set to true, game is CASINO and banker does not change, if set to false, game is FRIENDLY with a round robin banker
     * @param simFunction function to run
     * @param <T> player type
     * @param <RuleSet> rule set can be anything
     * @return players which are still in the game
     */
    public  <T extends Gambler, RuleSet> List<T> runManyGames(List<Domino> deck, List<T> gamblers, int maxSimCount, RuleSet ruleSet, boolean casinoGame, SimulationFunctionWithRuleSet<T, RuleSet> simFunction) {
        List<T> players = new ArrayList<>(gamblers);
        int sim = maxSimCount;
        int banker = 0;
        game_loop:
        while (players.size() > 1 && sim-- > 0) {
            System.out.println("---------" + (maxSimCount - sim) + "-------------");
            if (!casinoGame) {
                System.out.println(players.get(banker).getName() + " is a Banker");
            }
            Collections.shuffle(deck);

            final RunResult runResult = simFunction.runSimulation(deck, players, ruleSet, banker);
            if (runResult == RunResult.STOP_THE_GAME) {
                break game_loop;
            }

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
                int nextBanker = runResult == RunResult.CHANGE_BANKER ? banker + 1 : banker;
                for (int i = 0; i <= banker; i++) {
                    if (players.get(i).isBankrupt()) {
                        nextBanker--;
                    }
                }

                players = players.stream().filter(Gambler::stillHasMoney).collect(Collectors.toList());
                banker = (nextBanker) % players.size();
            }
        }
        System.out.println("----------END-----------");

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
