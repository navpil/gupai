package io.github.navpil.gupai.tricks.shiwuhu;

import io.github.navpil.gupai.util.ConsoleInput;
import io.github.navpil.gupai.Domino;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class HumanPlayer extends AbstractPlayer {

    private final ConsoleInput consoleInput;

    public HumanPlayer(String name) {
        super(name);
        consoleInput = new ConsoleInput();
    }

    @Override
    public void deal(Collection<Domino> dominos) {
        super.deal(dominos);
        Collections.sort(this.dominos);
    }

    @Override
    public List<Domino> lead() {
        if (this.dominos.isEmpty()) {
            return Collections.emptyList();
        }
        List<Domino> dominos;
        do {
            dominos = consoleInput.multiChoice(this.dominos, false, "What will you lead to the trick?");
        } while (!ShiWuHuHandHelper.isValidHand(dominos));
        return dominoesRemoved(dominos);
    }

    @Override
    public List<Domino> beat(List<Domino> lead) {
        List<Domino> dominos;
        do {
            dominos = consoleInput.multiChoice(this.dominos, true, "How will you beat the trick " + lead + "?");
        } while (!(dominos.isEmpty() || (ShiWuHuHandHelper.isValidHand(dominos) && canBeat(lead, dominos))));
        return dominoesRemoved(dominos);
    }

    private boolean canBeat(List<Domino> lead, List<Domino> dominos) {
        return new MultiSuitMove(dominos).beats(new MultiSuitMove(lead));
    }
}
