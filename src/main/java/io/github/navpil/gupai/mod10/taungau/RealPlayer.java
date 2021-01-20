package io.github.navpil.gupai.mod10.taungau;

import io.github.navpil.gupai.util.ConsoleInput;
import io.github.navpil.gupai.Domino;
import io.github.navpil.gupai.mod10.Mod10Rule;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class RealPlayer extends AbstractPlayer {

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
        List<Domino> discard;
        do {
            discard = consoleInput.multiChoice(new ArrayList<>(dominos), true, "Which dominos you'd like to discard (choose 3 or none)?");
        } while (!discardIsValid(discard));
        return withRemoved(discard);
    }

    private Collection<Domino> withRemoved(List<Domino> discard) {
        dominos.strictRemoveAll(discard);
        return discard;
    }

    private boolean discardIsValid(List<Domino> discard) {
        if (discard.isEmpty()) {
            return true;
        }
        if (discard.size() != 3) {
            return false;
        }
        return Mod10Rule.TAU_NGAU.getPoints(discard).isMod10();
    }
}
