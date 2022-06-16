package io.github.navpil.gupai.mod10.taungau;

import io.github.navpil.gupai.ChineseDominoSet;
import io.github.navpil.gupai.Domino;
import io.github.navpil.gupai.mod10.Mod10Rule;
import io.github.navpil.gupai.mod10.RunGamblingGame;
import io.github.navpil.gupai.util.ConsoleOutput;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class TauNgau {

    public static final Mod10Rule MOD_10_RULE = Mod10Rule.TAU_NGAU;
    private static final ConsoleOutput CONSOLE = new ConsoleOutput(false, false);

    public static void main(String[] args) {
        final List<Domino> deck = ChineseDominoSet.create();
        final List<Player> gamblers = List.of(
                new BankerPlayer("Banker"),
                new ComputerPlayer("Comp-2", 1000),
                new ComputerPlayer("Comp-1", 1000),
                new ComputerPlayer("Comp-3", 1000)
//                new RealPlayer("Jim", 100)
        );
        ArrayList<Player> copy = new ArrayList<>(gamblers);
        new RunGamblingGame().runManyGames(deck, gamblers, 10000, null, true,
                (dominos, players, ruleSet, banker) -> runSimulation(dominos, players, banker, TauNgauRuleSet.macao()));

        for (Player player : copy) {
            CONSOLE.sayAlways(player.getName() + " has " + player.getMoney());
        }
    }

    public static RunGamblingGame.RunResult runSimulation(List<Domino> dominos, List<Player> players, int bankerIndex, TauNgauRuleSet ruleSet) {

        final HashMap<String, Integer> bets = new HashMap<>();
        final HashMap<String, Boolean> discards = new HashMap<>();

        //Bets
        for (int i = 0; i < players.size(); i++) {
            if (i == bankerIndex) {
                continue;
            }
            final Player player = players.get(i);
            bets.put(player.getName(), ruleSet.getFixedStake() > 0 ? ruleSet.getFixedStake() : player.placeBet());
        }
        //Deal
        for (int i = 0; i < players.size(); i++) {
            players.get(i).deal(dominos.subList(i * 5, (i + 1) * 5));
        }
        //Discard
        for (final Player player : players) {
            Collection<Domino> discard = player.discard();
            CONSOLE.say(player.getName() + " discarded " + (discard.isEmpty() ? "nothing" : discard));
            discards.put(player.getName(), !discard.isEmpty() && isValid(discard));
        }

        //Calculation
        final Player banker = players.get(bankerIndex);
        boolean bankerDiscarded = discards.get(banker.getName());
        int bankerPoints = bankerDiscarded ? ruleSet.getMod10Rule().getPoints(banker.hand()).getMax() : -1;
        if (bankerDiscarded) {
            CONSOLE.say("Banker has a hand of " + banker.hand());
        } else {
            CONSOLE.say("Banker did not discard");
        }
        for (int i = 0; i < players.size(); i++) {
            if (i == bankerIndex) {
                continue;
            }
            final Player player = players.get(i);
            final boolean playerDiscarded = discards.get(player.getName());
            final Integer stake = bets.get(player.getName());
            CONSOLE.say(player.getName() + " has a hand of " + player.hand());

            int multiplier = ruleSet.getCalculation().calculatePlayerWinning(
                    playerDiscarded,
                    bankerDiscarded,
                    playerDiscarded ? ruleSet.getMod10Rule().getPoints(player.hand()).getMax() : -1,
                    bankerPoints);

            int wonAmount = stake * multiplier;
            if (wonAmount > 0) {
                CONSOLE.say(player.getName() + " won " + wonAmount);
            } else if (wonAmount < 0) {
                CONSOLE.say(player.getName() + " lost " + (-wonAmount));
            } else {
                CONSOLE.say(player.getName() + " pushed");
            }
            player.win(wonAmount);
            banker.lose(wonAmount);
        }
        CONSOLE.say("" + players.stream().map(p -> p.getName() + ": " + p.getMoney()).collect(Collectors.toList()));
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
