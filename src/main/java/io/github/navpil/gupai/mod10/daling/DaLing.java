package io.github.navpil.gupai.mod10.daling;

import io.github.navpil.gupai.ChineseDominoSet;
import io.github.navpil.gupai.dominos.Domino;
import io.github.navpil.gupai.dominos.DominoUtil;
import io.github.navpil.gupai.mod10.Mod10Rule;
import io.github.navpil.gupai.mod10.RunGamblingGame;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class DaLing {

    private final RuleSet ruleSet;

    public DaLing(RuleSet ruleSet) {
        this.ruleSet = ruleSet;
    }

    public static void main(String[] args) {
        final List<Domino> deck = ChineseDominoSet.create();
        final int money = 500;
        final List<Player> gamblers = List.of(
                new BankerPlayer("Banker"),
                new ComputerPlayer("Comp-1", money, false),
                new ComputerPlayer("Comp-2", money, false),
                new ComputerPlayer("Comp-3", money, true),
                new ComputerPlayer("Comp-4", money, true),
//                new ComputerPlayer("Comp-5", money, true)
                new HumanPlayer("Mark", money)
        );
        final DaLing daLing = new DaLing(RuleSet.macaoWithBanker());
        new RunGamblingGame().runManyGames(deck, gamblers, 100, true, daLing::runSimulation);
    }

    public static class PlayersGroup {

        private final ArrayList<Player> players;
        private final Player banker;

        public PlayersGroup(List<Player> allPlayers, int bankerIndex) {
            players = new ArrayList<>();
            for (int i = 0; i < allPlayers.size(); i++) {
                if (i == bankerIndex) continue;
                players.add(allPlayers.get(i));
            }
            if (bankerIndex >= 0 && bankerIndex < allPlayers.size()) {
                banker = allPlayers.get(bankerIndex);
            } else {
                banker = null;
            }
        }

        public ArrayList<Player> getPlayers() {
            return players;
        }

        public Player getBanker() {
            return banker;
        }
    }

    public RunGamblingGame.RunResult runSimulation(List<Domino> deck, List<Player> allPlayers, int bankerIndex) {

        PlayersGroup playersGroup = new PlayersGroup(allPlayers, bankerIndex);
        if (ruleSet.isExactPointGame() && !ruleSet.isLetBankerWinNonMatches() && playersGroup.getPlayers().size() == 1) {
            return RunGamblingGame.RunResult.STOP_THE_GAME;
        }
        final ArrayList<Domino> dominos = new ArrayList<>(deck);
        final HashMap<String, DaLingBet> bets = new HashMap<>();
        final HashMap<String, DaLingHand> hands = new HashMap<>();


        //Bets
        for (Player player : playersGroup.getPlayers()) {
            if (ruleSet.getFixedBet() != null) {
                bets.put(player.getName(), ruleSet.getFixedBet());
            } else {
                bets.put(player.getName(), player.placeBets());
            }
        }
        //Deal
        final List<Domino> firstTwoDominos = dominos.subList(0, 2);
        final List<Domino> bankersPair = new ArrayList<>(firstTwoDominos);
        firstTwoDominos.clear();

        for (Player player : playersGroup.getPlayers()) {
            final List<Domino> deal = dominos.subList(0, 6);
            player.deal(deal);
            deal.clear();
        }

        final Table table = new Table(ruleSet, bankersPair, bets);

        //Decide on hands
        for (Player player : playersGroup.getPlayers()) {
            DaLingHand hand = player.getHand(ruleSet.isExactPointGame() ? table : null);
            hands.put(player.getName(), hand);
        }

        //Calculation
        int bankerPoints = table.getBankerPoints();
        System.out.println("Banker got " + bankersPair + " which results in " + bankerPoints);

        if (ruleSet.isExactPointGame()) {
            exactPointsCalculation(playersGroup, bets, hands, table);
        } else {
            usualPointsCalculation(playersGroup, bets, hands, bankerPoints);
        }
        System.out.println(allPlayers.stream().map(p -> p.getName() + ": " + p.getMoney()).collect(Collectors.toList()));
        return RunGamblingGame.RunResult.CHANGE_BANKER;
    }

    private void usualPointsCalculation(PlayersGroup group, HashMap<String, DaLingBet> bets, HashMap<String, DaLingHand> hands,int bankerPoints) {
        Player banker = group.getBanker();
        for (Player player : group.getPlayers()) {
            final DaLingBet bet = bets.get(player.getName());
            final DaLingHand hand = hands.get(player.getName());
            for (int j = 0; j < 3; j++) {
                final Integer stake = bet.getBet(j);
                final Collection<Domino> dominoes = hand.getStack(j);
                final Integer playerPoints = Mod10Rule.DA_LING.getPoints(dominoes).getMax();
                System.out.println(player.getName() + " got " + dominoes + " (" + (j + 1) + ")");
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
            }

        }
    }

    private void exactPointsCalculation(PlayersGroup players, HashMap<String, DaLingBet> bets, HashMap<String, DaLingHand> hands, Table table) {
        Player banker = players.getBanker();
        final WinnerResult absoluteWinner = findAbsoluteWinner(players, table, hands);
        if (absoluteWinner.pushed) {
            System.out.println("Have several absolute winners, pushing");
        } else if (absoluteWinner.getWinner() != null) {
            int totalWin = 0;
            for (Player player : players.getPlayers()) {
                if (!player.equals(absoluteWinner.getWinner())) {
                    final int sum = bets.get(player.getName()).getTotal();
                    player.lose(sum);
                    totalWin += sum;
                }
            }
            if (ruleSet.isLetBankerWinNonMatches()) {
                int bankerLost = ruleSet.getBankerLossOnAbsoluteWinner();
                absoluteWinner.getWinner().win(totalWin + bankerLost);
                banker.lose(bankerLost);
            } else {
                absoluteWinner.getWinner().win(totalWin);
            }
            final String name = absoluteWinner.getWinner().getName();
            System.out.println("Absolute winner " + name + " won all stakes with a hand " + hands.get(name));
        } else {
            for (int stackIndex = 0; stackIndex < 3; stackIndex++) {
                final WinnerResult usualWinner = findUsualWinner(players, stackIndex, table, hands);
                final Player bestPlayer = usualWinner.getWinner();
                if (ruleSet.isLetBankerWinNonMatches() && bestPlayer == null) {
                    for (Player player : players.getPlayers()) {
                        final Integer bet = bets.get(player.getName()).getBet(stackIndex);
                        banker.win(bet);
                        player.lose(bet);
                    }
                } else if (!usualWinner.isPushed() && bestPlayer != null) {
                    int betSum = 0;
                    for (Player player : players.getPlayers()) {
                        final Integer betByPlayer = bets.get(player.getName()).getBet(stackIndex);
                        betSum += betByPlayer;
                        player.lose(betByPlayer);
                    }
                    System.out.println("Player " + bestPlayer.getName() + " won (" + stackIndex + ")");
                    bestPlayer.win(betSum);
                } else {
                    System.out.println("Pushed");
                }
            }
        }
    }

    public static WinnerResult findUsualWinner(PlayersGroup players, int stackIndex, Table table, HashMap<String, DaLingHand> hands) {
        final int bankerPoints = table.getBankerPoints();
        final Table.GameColor tableColor = table.getColor();
        int maxPips = -1;
        Player bestPlayer = null;
        boolean pushed = false;
        for (Player player : players.getPlayers()) {
            final DaLingHand hand = hands.get(player.getName());
            final Collection<Domino> part = hand.getStack(stackIndex);

            final boolean canBeExactly = Mod10Rule.DA_LING.getPoints(part).canBeExactly(bankerPoints);
            if (canBeExactly) {
                int currentPips = tableColor == Table.GameColor.RED ? part.stream().mapToInt(DominoUtil::redPointsCount).sum() : part.stream().mapToInt(DominoUtil::blackPointsCount).sum();
                System.out.println(player.getName() + " got " + part + " (" + ("can match " + bankerPoints) + ") on " + tableColor + " table with pips " + currentPips);
                if (currentPips > maxPips) {
                    pushed = false;
                    bestPlayer = player;
                    maxPips = currentPips;
                } else if (currentPips == maxPips) {
                    pushed = true;
                }
            } else {
                System.out.println(player.getName() + " got " + part + " (" + "no match" + ")");
            }
        }
        return new WinnerResult(bestPlayer, pushed);
    }

    public static WinnerResult findAbsoluteWinner(PlayersGroup players, Table table, Map<String, DaLingHand> hands) {
        final int bankerPoints = table.getBankerPoints();
        final Table.GameColor color = table.getColor();
        Player absoluteWinner = null;
        int pipCount = -1;
        boolean pushed = false;
        for (Player player : players.getPlayers()) {
            final String name = player.getName();
            final boolean canWin = hands.get(name).allMatchExactly(bankerPoints);
            if (canWin) {
                int currentPipCount = hands.get(name).getTotalPipCountFor(color);
                System.out.println("Possible absolute winner " + name + " with a hand " + hands.get(name) + " and pip count " + currentPipCount);
                if (absoluteWinner == null) {
                    absoluteWinner = player;
                    pipCount = currentPipCount;
                } else if (currentPipCount > pipCount) {
                    pushed = false;
                    absoluteWinner = player;
                    pipCount = currentPipCount;
                } else if (currentPipCount == pipCount) {
                    pushed = true;
                }
            }
        }
        return new WinnerResult(absoluteWinner, pushed);
    }

    public static class WinnerResult {
        private final Player winner;
        private final boolean pushed;

        public WinnerResult(Player winner, boolean pushed) {
            this.winner = winner;
            this.pushed = pushed;
        }

        public Player getWinner() {
            return winner;
        }

        public boolean isPushed() {
            return pushed;
        }
    }


}
