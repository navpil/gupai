package io.navpil.github.zenzen.util;

import io.navpil.github.zenzen.jielong.player.Counter;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class HashBag<T> extends AbstractBag<T> {

    private final Map<T, Counter> map;

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
    protected Map<T, Counter> getMap() {
        return map;
    }

    @Override
    protected HashBag<T> constructorCall() {
        return new HashBag<>();
    }
}
