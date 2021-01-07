package io.github.navpil.gupai.util;

import java.util.AbstractCollection;
import java.util.Collection;
import java.util.Iterator;

public class CombineCollection<T> extends AbstractCollection<T> {

    private final Collection<Collection<T>> metaCollection;

    public CombineCollection(Collection<Collection<T>> metaCollection) {
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

        private Iterator<T> currentIterator;
        private final Iterator<Collection<T>> metaIterator;

        public It() {
            metaIterator = metaCollection.iterator();
            findNextIterator();
        }

        private void findNextIterator() {
            boolean shouldFindIterator = true;
            while (shouldFindIterator) {
                if (metaIterator.hasNext()) {
                    final Collection<T> c = metaIterator.next();
                    final Iterator<T> cIt = c.iterator();
                    if (cIt.hasNext()) {
                        currentIterator = cIt;
                        shouldFindIterator = false;
                    }
                } else {
                    currentIterator = null;
                    shouldFindIterator = false;
                }
            }
        }

        @Override
        public boolean hasNext() {
            if (currentIterator == null) {
                return false;
            } else {
                return currentIterator.hasNext();
            }
        }

        @Override
        public T next() {
            final T t = currentIterator.next();
            if (!currentIterator.hasNext()) {
                findNextIterator();
            }
            return t;
        }
    }
}
