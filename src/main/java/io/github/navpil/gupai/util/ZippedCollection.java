package io.github.navpil.gupai.util;

import io.github.navpil.gupai.fishing.CircularInteger;

import java.util.AbstractCollection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class ZippedCollection<T> extends AbstractCollection<T> {

    private final Collection<Collection<T>> metaCollection;

    public ZippedCollection(Collection<Collection<T>> metaCollection) {
        this.metaCollection = metaCollection;
    }

    @Override
    public Iterator<T> iterator() {
        return new It();
    }

    @Override
    public int size() {
        return metaCollection.stream().map(Collection::size).reduce(Integer::sum).orElse(0);
    }

    private class It implements Iterator<T> {

        private CircularInteger counter;
        private List<Iterator<T>> allIterators = new ArrayList<>();

        public It() {
            counter = new CircularInteger(metaCollection.size(), 0);
            for (Collection<T> ts : metaCollection) {
                allIterators.add(ts.iterator());
            }
        }

        @Override
        public boolean hasNext() {
            return allIterators.stream().anyMatch(Iterator::hasNext);
        }

        @Override
        public T next() {
            int unsuccessfulCounter = 0;
            while (!allIterators.get(counter.current()).hasNext() || unsuccessfulCounter < allIterators.size()) {
                unsuccessfulCounter++;
                counter.next();
            }
            final Iterator<T> currentIterator = allIterators.get(counter.current());
            if (currentIterator.hasNext()) {
                counter.next();
                return currentIterator.next();
            } else {
                throw new IllegalStateException();
            }
        }
    }
}
