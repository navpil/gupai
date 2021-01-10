package io.github.navpil.gupai.mod10;

import io.github.navpil.gupai.ConsoleInput;
import io.github.navpil.gupai.ChineseDominoSet;
import io.github.navpil.gupai.dominos.Domino;
import io.github.navpil.gupai.util.CollectionUtil;
import io.github.navpil.gupai.util.HashBag;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class TauNgau {

    public static final Mod10Rule MOD_10_RULE = Mod10Rule.TAU_NGAU;

    public interface Player extends RunGamblingGame.Gambler {

        void deal(Collection<Domino> dominos);

        int placeBet();

        Collection<Domino> discard();

        Collection<Domino> hand();

        void lose(int stake);

        void win(int stake);

        @Override
        int getMoney();
    }

    public static abstract class AbstractPlayer implements Player {

        private final String name;
        private int money;
        protected HashBag<Domino> dominos;

        protected AbstractPlayer(String name, int money) {
            this.name = name;
            this.money = money;
        }

        @Override
        public void deal(Collection<Domino> dominos) {
            this.dominos = new HashBag<>(dominos);
        }

        @Override
        public Collection<Domino> hand() {
            return dominos;
        }

        @Override
        public void lose(int stake) {
            money -= stake;
        }

        @Override
        public void win(int stake) {
            money += stake;
        }

        @Override
        public int getMoney() {
            return money;
        }

        @Override
        public String getName() {
            return name;
        }
    }

    public static class RealPlayer extends AbstractPlayer {

        private final ConsoleInput consoleInput;

        protected RealPlayer(String name, int money) {
            super(name, money);
            consoleInput = new ConsoleInput();
        }

        @Override
        public int placeBet() {
            return consoleInput.readInt(
                    integer -> integer <= getMoney(),
                    "What's your stake?",
                    "That's too much, you only have " + getMoney()
            );
        }

        @Override
        public Collection<Domino> discard() {
            final ArrayList<Domino> dominoList = new ArrayList<>(dominos);
            final StringBuilder sb = new StringBuilder("Which dominos you'd like to discard (strictly comma-separated)?\n");
            for (int i = 0; i < dominoList.size(); i++) {
                sb.append(i + 1).append(") ").append(dominoList.get(i)).append("\n");
            }

            final String discardAsString = consoleInput.readString((s) -> {
                        if ("0".equals(s)) {
                            return true;
                        }
                        final Set<String> validValues = Set.of("1", "2", "3", "4", "5");
                        final List<String> values = Arrays.asList(s.split(","));
                        final HashSet<String> set = new HashSet<>(values);
                        return set.size() == 3 && validValues.containsAll(values);
                    }, sb.toString(), "Invalid input"
            );
            final List<Integer> indexes = Arrays.stream(discardAsString.split(",")).map(Integer::parseInt).collect(Collectors.toList());
            if (indexes.size() == 1) {
                return Collections.emptyList();
            } else {
                final List<Domino> discard = indexes.stream().map(i -> dominoList.get(i - 1)).collect(Collectors.toList());
                this.dominos.strictRemoveAll(discard);
                return discard;
            }
        }
    }

    public static class ComputerPlayer extends AbstractPlayer {

        public ComputerPlayer(String name, int money) {
            super(name, money);
        }

        @Override
        public int placeBet() {
            return Math.min(getMoney(), 3);
        }

        @Override
        public Collection<Domino> discard() {
            final Collection<Collection<Domino>> possibleDiscards = CollectionUtil.allPermutations(dominos, 3);
            final Collection<Collection<Domino>> dominos = possibleDiscards.stream().filter(d -> MOD_10_RULE.getPoints(d).isMod10()).collect(Collectors.toSet());
            final Collection<Domino> discard = calculateBestDiscard(dominos);
            this.dominos.strictRemoveAll(discard);
            return discard;
        }

        private Collection<Domino> calculateBestDiscard(Collection<Collection<Domino>> dominos) {
            if (dominos.isEmpty()) {
                return Collections.emptyList();
            } else if (dominos.size() == 1) {
                return dominos.iterator().next();
            } else {
                final Iterator<Collection<Domino>> discards = dominos.iterator();
                Collection<Domino> discard = discards.next();
                long supremesCount = discard.stream().filter(Mod10Rule.SUPREMES::contains).count();
                if (supremesCount == 0) {
                    return discard;
                }
                while (discards.hasNext()) {
                    final Collection<Domino> currentDiscard = discards.next();
                    final long currentSupremesCount = discard.stream().filter(Mod10Rule.SUPREMES::contains).count();
                    if (currentSupremesCount == 0) {
                        return currentDiscard;
                    }
                    if (currentSupremesCount < supremesCount) {
                        supremesCount = currentSupremesCount;
                        discard = currentDiscard;
                    }
                }
                return discard;
            }
        }

    }

    public static class BankerPlayer extends ComputerPlayer {

        public BankerPlayer(String name) {
            super(name, 0);
        }

        @Override
        public boolean stillHasMoney() {
            return true;
        }

        @Override
        public boolean isBankrupt() {
            return false;
        }
    }

    public static void main(String[] args) {
        final List<Domino> deck = ChineseDominoSet.create();
        final List<Player> gamblers = List.of(
                new BankerPlayer("Banker"),
                new ComputerPlayer("Comp-2", 100),
                new RealPlayer("Jim", 100)
        );
        new RunGamblingGame().runManyGames(deck, gamblers, 100, null, true, (dominos, players, ruleSet, banker) -> runSimulation(dominos, players, banker));
    }

    public static RunGamblingGame.ChangeBanker runSimulation(List<Domino> dominos, List<Player> players, int bankerIndex) {

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
            return RunGamblingGame.ChangeBanker.TRUE;
        } else {
            return RunGamblingGame.ChangeBanker.FALSE;
        }

    }

    private static boolean isValid(Collection<Domino> discard) {
        if (discard.size() != 3) {
            return false;
        }
        return Mod10Rule.TAU_NGAU.getPoints(discard).isMod10();
    }


}
