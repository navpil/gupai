package io.navpil.github.zenzen.fishing;

import io.navpil.github.zenzen.ConsoleInput;
import io.navpil.github.zenzen.dominos.Domino;
import io.navpil.github.zenzen.util.Bag;
import io.navpil.github.zenzen.util.HashBag;
import io.navpil.github.zenzen.util.TreeBag;

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
        final StringBuilder sb = new StringBuilder("Which domino will you use as a bait?\n");
        int counter = 0;
        final Domino[] dominos = new Domino[this.dominos.size()];
        for (Domino domino : this.dominos) {
            dominos[counter++] = domino;
            sb.append(counter).append(") ").append(domino).append("\n");
        }
        final int size = this.dominos.size();
        final String prompt = sb.toString();
        final int baitIndex = consoleInput.readInt(
                (i) -> i > 0 && i <= size,
                prompt,
                "Invalid input"
        );
        final Domino bait = dominos[baitIndex - 1];
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
            final ArrayList<Catch> catchList = new ArrayList<>(catches);
            final StringBuilder sb = new StringBuilder("What would you like to catch?\n");
            for (int i = 0; i < catchList.size(); i++) {
                sb.append(i + 1).append(") ").append(catchList.get(i)).append("\n");
            }
            final String prompt = sb.toString();
            final int catchIndex = consoleInput.readInt(
                    i -> i > 0 && i <= catchList.size(),
                    prompt,
                    "Invalid input"
            );
            c = catchList.get(catchIndex - 1);
        }
        if (ownBait) {
            dominos.remove(bait);
        }
        return c;
    }

}
