package io.navpil.github.zenzen.fishing;

import io.navpil.github.zenzen.dominos.Domino;
import io.navpil.github.zenzen.util.Bag;
import io.navpil.github.zenzen.util.CombineCollection;
import io.navpil.github.zenzen.util.HashBag;
import io.navpil.github.zenzen.util.TreeBag;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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
            final List<Domino> previousCatches = table.getCatch(name).stream().flatMap(c -> c.getDominos().stream()).collect(Collectors.toList());
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
