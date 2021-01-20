package io.github.navpil.gupai.rummy.jjakmatchugi;

import io.github.navpil.gupai.Domino;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class Table {

    private final RuleSet ruleSet;
    private final Collection<Domino> discards = new ArrayList<>();
    private Domino lastDiscard;
    private final Map<Integer, Collection<Domino>> combinations = new HashMap<>();

    public Table(RuleSet ruleSet) {
        this.ruleSet = ruleSet;
    }

    public void addDiscard(Domino discard) {
        if (lastDiscard != null) {
            discards.add(lastDiscard);
        }
        lastDiscard = discard;
    }

    public Domino lastDiscard() {
        return lastDiscard;
    }

    public void addCombination(int current, Collection<Domino> combination) {
        if (!combinations.containsKey(current)) {
            combinations.put(current, new ArrayList<>());
        }
        combinations.get(current).addAll(combination);
    }

    public Collection<Domino> getCombinations(int player) {
        if (!combinations.containsKey(player)) {
            combinations.put(player, new ArrayList<>());
        }
        return combinations.get(player);
    }

    public RuleSet getRuleSet() {
        return ruleSet;
    }

    public Collection<Domino> allDeadTiles() {
        return discards;
        //There is no point in fetching all the shown dominoes - those are pairs and can only match each other
//        return new CombineCollection<>(List.of(discards, dominoes.values().stream().flatMap(Collection::stream).collect(Collectors.toList())));
    }

    public void resetLastDiscard() {
        lastDiscard = null;
    }
}
