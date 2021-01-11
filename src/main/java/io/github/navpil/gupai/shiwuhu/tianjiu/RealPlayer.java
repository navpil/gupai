package io.github.navpil.gupai.shiwuhu.tianjiu;

import io.github.navpil.gupai.ConsoleInput;
import io.github.navpil.gupai.dominos.Domino;

import java.util.Collection;
import java.util.List;

public class RealPlayer extends AbstractPlayer {

    private final ConsoleInput consoleInput;
    private HandHelper handHelper;

    public RealPlayer(String name) {
        super(name);
        consoleInput = new ConsoleInput();
    }

    @Override
    public void showTable(Table table) {
        super.showTable(table);
        handHelper = new HandHelper(table.getRuleSet());
    }

    @Override
    public Collection<Domino> lead() {
        List<Domino> dominos;
        do {
            dominos = consoleInput.multiChoice(this.dominos, false, "Choose a lead");
        } while (!handHelper.isValidHand(dominos));
        return dominoesReturned(dominos);
    }

    @Override
    public Collection<Domino> beat(Collection<Domino> lead) {
        List<Domino> dominos;
        do {
            dominos = consoleInput.multiChoice(this.dominos, true, "Choose how to beat a " + lead);
        } while (!(dominos.isEmpty() || handHelper.canBeat(lead, dominos)));
        return dominoesReturned(dominos);
    }

    @Override
    public Collection<Domino> discard(Collection<Domino> lead) {
        List<Domino> dominos;
        do {
            dominos = consoleInput.multiChoice(this.dominos, false, "Choose what to discard for a " + lead);
        } while (dominos.size() != lead.size());
        return dominoesReturned(dominos);
    }

    private Collection<Domino> dominoesReturned(List<Domino> dominos) {
        for (Domino domino : dominos) {
            this.dominos.remove(domino);
        }
        return dominos;
    }
}
