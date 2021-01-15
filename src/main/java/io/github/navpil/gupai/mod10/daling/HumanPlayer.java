package io.github.navpil.gupai.mod10.daling;

import io.github.navpil.gupai.ConsoleInput;
import io.github.navpil.gupai.dominos.Domino;

import java.util.List;

public class HumanPlayer extends AbstractPlayer {

    private final ConsoleInput consoleInput;

    public HumanPlayer(String name, int money) {
        super(name, money);
        consoleInput = new ConsoleInput();
    }

    @Override
    public DaLingBet placeBets() {
        List<Integer> bets;
        do {
            bets = getBets();
        } while (bets.stream().filter(i -> i != 0).findAny().isEmpty());
        return new DaLingBet(bets);
    }

    private List<Integer> getBets() {
        final int singleBet = consoleInput.readInt(i -> i >= 0 && i < money, "Place a bet on a single tile", "Invalid input");
        final int pairBet = consoleInput.readInt(i -> i >= 0 && i < (money - singleBet), "Place a bet on a pair", "Invalid input");
        final int tripleBet = consoleInput.readInt(i -> i >= 0 && i < (money - singleBet - pairBet), "Place a bet on a triplet", "Invalid input");
        return List.of(singleBet, pairBet, tripleBet);
    }

    @Override
    public DaLingHand getHand(Table table) {
        if (table != null) {
            System.out.println("Table has " + table.getBankerPoints() + " with color " + table.getColor());
        }
        final Domino tile = consoleInput.choice(dominos, false, "Place a single tile");
        dominos.remove(tile);
        final List<Domino> pair = consoleInput.multiChoice(dominos, false, "Place a pair tile", 2);
        for (Domino domino : pair) {
            dominos.remove(domino);
        }
        return new DaLingHand(tile, pair, dominos);
    }
}
