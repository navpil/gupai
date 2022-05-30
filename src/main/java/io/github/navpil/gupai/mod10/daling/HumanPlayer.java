package io.github.navpil.gupai.mod10.daling;

import io.github.navpil.gupai.util.ConsoleInput;
import io.github.navpil.gupai.Domino;

import java.util.List;

public class HumanPlayer extends AbstractPlayer {

    private final ConsoleInput consoleInput;

    public HumanPlayer(String name, int money) {
        super(name, money);
        consoleInput = new ConsoleInput();
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
