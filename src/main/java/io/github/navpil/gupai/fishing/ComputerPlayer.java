package io.github.navpil.gupai.fishing;

import io.github.navpil.gupai.util.CombineCollection;
import io.github.navpil.gupai.dominos.Domino;
import io.github.navpil.gupai.util.Bag;
import io.github.navpil.gupai.util.HashBag;
import io.github.navpil.gupai.util.TreeBag;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * AI player which tries to catch the best possible Catch, but does not really think much into the future, so
 * it does not try to collect Juns for example.
 */
public class ComputerPlayer implements Player {

    private final String name;
    private Bag<Domino> dominos;
    private Table table;

    public ComputerPlayer(String name) {
        this.name = name;
    }

    @Override
    public void deal(Collection<Domino> deal) {
        this.dominos = new TreeBag<>(deal);
    }

    @Override
    public void showTable(Table table) {
        this.table = table;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Catch fish(Bag<Domino> pool) {
        return calculateBestCatch(dominos, pool, true);
    }

    private Catch calculateBestCatch(Collection<Domino> baits, Bag<Domino> pool, boolean ownBait) {
        final Set<Catch> catches = Util.allPossibleCatches(baits, pool, table.getRuleSet());
        if (catches.isEmpty()) {
            Domino min = null;
            int minPoints = 100;
            for (Domino domino : baits) {
                final int points = points(domino);
                if (points < minPoints) {
                    minPoints = points;
                    min = domino;
                }
            }
            if (ownBait) {
                dominos.remove(min);
            }
            return new Catch(min, HashBag.of());
        } else {
            final ArrayList<Catch> sortedCatches = new ArrayList<>(catches);
            sortedCatches.sort((c1, c2) -> Integer.compare(points(c2.getBait()), points(c1.getBait())));
            Catch maxCatch = null;
            int maxPoints = -1;
            final Collection<Domino> previousCatches = table.getCatch(name);
            for (Catch c : sortedCatches) {
                final int points = table.getRuleSet().calculatePoints(new CombineCollection<>(List.of(previousCatches, c.getDominos())));
                if (points > maxPoints) {
                    maxPoints = points;
                    maxCatch = c;
                }
            }
            if (ownBait) {
                dominos.remove(maxCatch.getBait());
            }
            return maxCatch;
        }
    }

    private int points(Domino d) {
        return d.getPips()[0] + d.getPips()[1];
    }

    @Override
    public Catch fish(Bag<Domino> pool, Domino bait) {
        return calculateBestCatch(List.of(bait), pool, false);
    }

    @Override
    public String toString() {
        return "Player{" +
                "name='" + name + '\'' +
                ", dominos=" + dominos +
                '}';
    }
}
