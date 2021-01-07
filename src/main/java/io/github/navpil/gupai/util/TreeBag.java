package io.github.navpil.gupai.util;

import io.github.navpil.gupai.jielong.player.MutableInteger;

import java.util.Collection;
import java.util.Map;
import java.util.TreeMap;

public class TreeBag<T> extends AbstractBag<T> {

    private final Map<T, MutableInteger> map;

    public TreeBag() {
        map = new TreeMap<>();
    }

    public TreeBag(Collection<T> ts) {
        map = new TreeMap<>();
        addAll(ts);
    }

    @Override
    protected Map<T, MutableInteger> getMap() {
        return map;
    }

    @Override
    protected TreeBag<T> constructorCall() {
        return new TreeBag<>();
    }
}
