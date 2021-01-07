package io.github.navpil.gupai.util;

import io.github.navpil.gupai.jielong.player.MutableInteger;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class HashBag<T> extends AbstractBag<T> {

    private final Map<T, MutableInteger> map;

    public HashBag() {
        map = new HashMap<>();
    }

    public HashBag(Collection<T> ts) {
        map = new HashMap<>();
        addAll(ts);
    }

    public static <T> HashBag<T> of(T ... elements) {
        final HashBag<T> ts = new HashBag<>();
        Collections.addAll(ts, elements);
        return ts;
    }

    @Override
    protected Map<T, MutableInteger> getMap() {
        return map;
    }

    @Override
    protected HashBag<T> constructorCall() {
        return new HashBag<>();
    }
}
