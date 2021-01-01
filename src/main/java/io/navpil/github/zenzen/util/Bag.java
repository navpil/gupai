package io.navpil.github.zenzen.util;

import java.util.Collection;

public interface Bag<T> extends Collection<T> {

    /**
     * Removes strictly given amount of elements
     *
     * @param elements elements to remove
     */
    void strictRemoveAll(Collection<T> elements);

    /**
     * Retains strictly given amount of elements
     *
     * @param elements elements to retain
     */
    void strictRetainAll(Collection<T> elements);
}
