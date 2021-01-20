package io.github.navpil.gupai.rummy.jjakmatchugi;

import io.github.navpil.gupai.util.ConsoleInput;
import io.github.navpil.gupai.Domino;

import java.util.ArrayList;
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
    public void deal(List<Domino> subList) {
        super.deal(subList);
        Collections.sort(dominos);
    }

    @Override
    public Collection<Domino> offer(Domino offer) {
        final RuleSet.Pairs pairsType = table.getRuleSet().getPairsType();
        Collection<Domino> combination = new ArrayList<>();
        do {
            final Domino choice = consoleInput.choice(dominos, true, "Which pair you'd like to create with " + offer);
            if (choice == null) {
                return Collections.emptyList();
            }
        } while (!pairsType.validPairCombination(combination));
        return removed(combination);
    }

    @Override
    public Domino discard() {
        return removed(consoleInput.choice(dominos, false, "Choose a domino to discard"));
    }


}
