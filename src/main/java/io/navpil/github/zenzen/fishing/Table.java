package io.navpil.github.zenzen.fishing;

import io.navpil.github.zenzen.dominos.Domino;
import io.navpil.github.zenzen.util.Bag;
import io.navpil.github.zenzen.util.HashBag;
import io.navpil.github.zenzen.util.TreeBag;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class Table {

    private final RuleSet ruleSet;
    private Bag<Domino> pool;
    private Map<String, Bag<Catch>> catches = new HashMap<>();

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
            pool.add(c.getBait());
        } else {
            remove(c.getFish());
            if (!catches.containsKey(name)) {
                catches.put(name, new HashBag<>());
            }
            catches.get(name).add(c);
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

    public Bag<Catch> getCatch(String name) {
        if (catches.get(name) == null) {
            catches.put(name, new HashBag<>());
        }
        return catches.get(name);
    }
}
