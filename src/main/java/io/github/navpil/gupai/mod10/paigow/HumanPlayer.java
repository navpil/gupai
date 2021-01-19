package io.github.navpil.gupai.mod10.paigow;

import io.github.navpil.gupai.ConsoleInput;
import io.github.navpil.gupai.dominos.Domino;
import io.github.navpil.gupai.util.HashBag;

import java.util.List;

public class HumanPlayer extends AbstractPlayer {

    private final ConsoleInput consoleInput;

    public HumanPlayer(String name, int money) {
        super(name, money);
        consoleInput = new ConsoleInput();
    }

    @Override
    protected int getStakeAmount(int max) {
        return consoleInput.readInt(i -> i > 0 && i <= max, "What's your stake?", "Invalid stake");
    }

    @Override
    public Hand hand() {
        final List<Domino> pair1 = consoleInput.multiChoice(dominos, false, "Choose a pair", 2);
        final HashBag<Domino> bag = new HashBag<>(this.dominos);
        bag.strictRemoveAll(pair1);
        return new Hand(new PaiGowPair(pair1), new PaiGowPair(bag));
    }
}
