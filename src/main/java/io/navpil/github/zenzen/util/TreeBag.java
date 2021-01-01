package io.navpil.github.zenzen.util;

import io.navpil.github.zenzen.jielong.player.Counter;

import java.util.Collection;
import java.util.Map;
import java.util.TreeMap;

public class TreeBag<T> extends AbstractBag<T> {

    private final Map<T, Counter> map;

    public TreeBag() {
        map = new TreeMap<>();
    }

    public TreeBag(Collection<T> ts) {
        map = new TreeMap<>();
        addAll(ts);
    }

    @Override
    protected Map<T, Counter> getMap() {
        return map;
    }

    @Override
    protected TreeBag<T> constructorCall() {
        return new TreeBag<>();
    }
}
