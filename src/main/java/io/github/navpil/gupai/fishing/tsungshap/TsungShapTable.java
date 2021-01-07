package io.github.navpil.gupai.fishing.tsungshap;

import io.github.navpil.gupai.dominos.Domino;
import io.github.navpil.gupai.util.Bag;
import io.github.navpil.gupai.util.HashBag;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class TsungShapTable {

    //First is left, Last is right
    // Left <- [First][Second]...[PreLast][Last] -> Right
    private final LinkedList<Domino> row = new LinkedList<>();
    private final Map<String, Bag<TsungShapCatch>> catches = new HashMap<>();
    private final TsungShapRuleSet ruleSet;

    public TsungShapTable(TsungShapRuleSet ruleSet) {
        this.ruleSet = ruleSet;

    }

    public TsungShapRuleSet getRuleSet() {
        return ruleSet;
    }

    public LinkedList<Domino> getRow() {
        return row;
    }

    public void addCatch(String name, TsungShapCatch c) {
        if (!catches.containsKey(name)) {
            catches.put(name, new HashBag<>());
        }
        catches.get(name).add(c);
    }

    public Collection<TsungShapCatch> getCatch(String name) {
        if (!catches.containsKey(name)) {
            catches.put(name, new HashBag<>());
        }
        return catches.get(name);
    }
}
