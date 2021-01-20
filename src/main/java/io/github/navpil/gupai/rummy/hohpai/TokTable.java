package io.github.navpil.gupai.rummy.hohpai;

import io.github.navpil.gupai.Domino;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class TokTable implements Table {

    private final LinkedList<Domino> dominos = new LinkedList<>();
    private final List<Domino> discards = new ArrayList<>();
    private final Map<Integer, Collection<Collection<Domino>>> combinations = new HashMap<>();

    private final RuleSet ruleSet;
    private Domino lastDiscard;

    public TokTable(List<Domino> subList, RuleSet ruleSet) {
        dominos.addAll(subList);
        this.ruleSet = ruleSet;
    }

    @Override
    public RuleSet getRuleSet() {
        return ruleSet;
    }

    @Override
    public Collection<Domino> getAllDeadDominoes() {
        final ArrayList<Domino> deads = new ArrayList<>(discards);
        if (lastDiscard != null) {
            deads.add(lastDiscard);
        }
        return deads;
    }

    public Domino remove() {
        return dominos.removeLast();
    }

    public void discard(Domino discard) {
        if (lastDiscard != null) {
            discards.add(lastDiscard);
        }
        lastDiscard = discard;
        dominos.add(discard);
    }

    public Domino lastDiscard() {
        return lastDiscard;
    }

    public void resetLastDiscard() {
        lastDiscard = null;
    }

    public void addCombination(int current, Collection<Domino> combination) {
        if (!combinations.containsKey(current)) {
            combinations.put(current, new ArrayList<>());
        }
        combinations.get(current).add(combination);
    }

    public Collection<Collection<Domino>> getCombinations(int player) {
        if (!combinations.containsKey(player)) {
            combinations.put(player, new ArrayList<>());
        }
        return combinations.get(player);
    }

}
