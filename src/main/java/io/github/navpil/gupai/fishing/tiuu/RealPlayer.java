package io.github.navpil.gupai.fishing.tiuu;

import io.github.navpil.gupai.util.ConsoleInput;
import io.github.navpil.gupai.Domino;
import io.github.navpil.gupai.util.Bag;
import io.github.navpil.gupai.util.HashBag;
import io.github.navpil.gupai.util.TreeBag;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

public class RealPlayer implements Player {

    private final String name;
    private final ConsoleInput consoleInput;
    private TreeBag<Domino> dominos;
    private RuleSet ruleSet;
    private Table table;

    public RealPlayer(String name) {
        this.name = name;
        consoleInput = new ConsoleInput();
    }

    @Override
    public void deal(Collection<Domino> deal) {
        dominos = new TreeBag<>(deal);
    }

    @Override
    public void showTable(Table table) {
        ruleSet = table.getRuleSet();
        this.table = table;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Catch fish(Bag<Domino> pool) {
        System.out.println("Your catch is " + table.getCatch(name));
        System.out.println("Table contains these dominoes " + pool);
        final Domino bait = consoleInput.choice(new ArrayList<>(dominos), false, "Which domino will you use as a bait?");
        return internalCatch(pool, bait, true);
    }

    @Override
    public Catch fish(Bag<Domino> pool, Domino bait) {
        System.out.println("Your current catch is " + table.getCatch(name));
        return internalCatch(pool, bait, false);
    }

    private Catch internalCatch(Bag<Domino> pool, Domino bait, boolean ownBait) {
        final Set<Catch> catches = Util.allPossibleCatches(List.of(bait), pool, ruleSet);
        Catch c;
        if (catches.isEmpty()) {
            c = new Catch(bait, HashBag.of());
        } else if (catches.size() == 1) {
            c = catches.iterator().next();
        } else {
            c = consoleInput.choice(new ArrayList<>(catches), false, "What would you like to catch?");
        }
        if (ownBait) {
            dominos.remove(bait);
        }
        return c;
    }

}
