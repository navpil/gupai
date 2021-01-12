package io.github.navpil.gupai.rummy.jjakmatchugi;

import io.github.navpil.gupai.dominos.Domino;
import io.github.navpil.gupai.util.CombineCollection;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public abstract class AbstractPlayer implements Player {

    private final String name;
    protected ArrayList<Domino> dominos;
    protected Table table;
    private ArrayList<Domino> winningHand;

    public AbstractPlayer(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Collection<Domino> getWinningHand() {
        if (winningHand != null) {
            return winningHand;
        }
        if (dominos.isEmpty()) {
            return Collections.emptyList();
        }
        if (table.getRuleSet().getPairsType().validPairCombination(dominos)) {
            winningHand = dominos;
        }
        return winningHand;
    }

    @Override
    public void deal(List<Domino> subList) {
        dominos = new ArrayList<>(subList);
        winningHand = null;
    }

    @Override
    public void showTable(Table table) {
        this.table = table;
    }

    @Override
    public void give(Domino domino) {
        dominos.add(domino);
    }

    @Override
    public Collection<Domino> extractPairs() {
        final List<Domino> civils = dominos.stream().filter(Domino::isCivil).sorted().collect(Collectors.toList());
        final ArrayList<Domino> civilCombinations = new ArrayList<>();
        for (int i = 1; i < civils.size(); i++) {
            if (civils.get(i - 1).equals(civils.get(i))) {
                civilCombinations.add(civils.get(i - 1));
                civilCombinations.add(civils.get(i));
            }
        }
        final Set<Domino> military = dominos.stream().filter(Domino::isMilitary).collect(Collectors.toSet());
        final RuleSet.Pairs pairsType = table.getRuleSet().getPairsType();
        final HashSet<Domino> militaryCombinations = new HashSet<>();
        for (Domino domino : military) {
            final Domino other = pairsType.other(domino);
            if (military.contains(other)) {
                militaryCombinations.add(domino);
                militaryCombinations.add(other);
            }
        }
        return removed(new CombineCollection<>(List.of(civilCombinations, militaryCombinations)));
    }

    protected Domino removed(Domino domino) {
        dominos.remove(domino);
        return domino;
    }

    protected Collection<Domino> removed(Collection<Domino> combination) {
        for (Domino domino : combination) {
            dominos.remove(domino);
        }
        return combination;
    }
}
