package io.github.navpil.gupai.fishing.tiuu;

import io.github.navpil.gupai.Domino;
import io.github.navpil.gupai.util.Bag;
import io.github.navpil.gupai.util.TreeBag;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class Table {

    private final RuleSet ruleSet;
    private Bag<Domino> pool;
    private Map<String, TreeBag<Domino>> catches = new HashMap<>();

    public Table(RuleSet ruleSet) {
        this.ruleSet = ruleSet;
    }

    public void setupPool(Collection<Domino> initialPool) {
        this.pool = new TreeBag<>(initialPool);
    }

    private void remove(Bag<Domino> fish) {
        pool.strictRemoveAll(fish);
    }

    public void apply(String name, Catch c) {
        if (c.getFish().isEmpty()) {
            //discard
            pool.add(c.getBait());
        } else {
            //fish caught
            remove(c.getFish());
            if (!catches.containsKey(name)) {
                catches.put(name, new TreeBag<>());
            }
            catches.get(name).addAll(c.getDominos());
        }
    }

    public Bag<Domino> getPool() {
        return pool;
    }

    public RuleSet getRuleSet() {
        return ruleSet;
    }

    @Override
    public String toString() {
        return "Table{" +
                "pool=" + pool +
                ", catches=" + catches +
                '}';
    }

    public Bag<Domino> getCatch(String name) {
        if (catches.get(name) == null) {
            catches.put(name, new TreeBag<>());
        }
        return catches.get(name);
    }
}
