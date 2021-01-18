package io.github.navpil.gupai.util;

import io.github.navpil.gupai.jielong.player.MutableInteger;

import java.util.AbstractCollection;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;

public abstract class AbstractBag<T> extends AbstractCollection<T> implements Bag<T> {

    private int size;

    @Override
    public boolean add(T t) {
        size++;
        if (getMap().containsKey(t)) {
            getMap().get(t).inc();
        } else {
            getMap().put(t, new MutableInteger(1));
        }
        return true;
    }

    protected abstract Map<T, MutableInteger> getMap();

    @Override
    public boolean contains(Object o) {
        return getMap().containsKey(o);
    }

    @Override
    public boolean remove(Object o) {
        final MutableInteger counter = getMap().get(o);
        if (counter == null) {
            return false;
        } else if (counter.getCount() == 1) {
            size--;
            getMap().remove(o);
            return true;
        } else {
            size--;
            counter.dec();
            return true;
        }
    }

    @Override
    public Iterator<T> iterator() {
        return new BagIterator();
    }

    @Override
    public int size() {
        return size;//getMap().values().stream().map(MutableInteger::getCount).reduce(Integer::sum).orElse(0);
    }

    public void strictRemoveAll(Collection<T> elements) {
        for (T t : elements) {
            remove(t);
        }
    }

    public void strictRetainAll(Collection<T> elements) {
        final AbstractBag<T> copy = constructorCall();
        copy.addAll(this);
        copy.strictRemoveAll(elements);
        this.strictRemoveAll(copy);
//        else {
//            AbstractBag<T> bag;
//            if (elements instanceof AbstractBag) {
//                bag = (AbstractBag<T>) elements;
//            } else {
//                bag = constructorCall();
//                bag.addAll(elements);
//            }
//            final Iterator<Map.Entry<T, Counter>> it = getMap().entrySet().iterator();
//            final Map<T, Counter> copyMap = bag.getMap();
//            while (it.hasNext()) {
//                final Map.Entry<T, Counter> next = it.next();
//                final Counter copyCounter = copyMap.get(next.getKey());
//                final Counter value = next.getValue();
//                if (copyCounter == null) {
//                    it.remove();
//                } else {
//                    value.set(Math.min(copyCounter.getCount(), value.getCount()));
//                }
//            }
//        }

    }

    protected abstract AbstractBag<T> constructorCall();

    public <T> int count(T key) {
        final MutableInteger mutableInteger = getMap().get(key);
        if (mutableInteger == null) {
            return 0;
        } else {
            return mutableInteger.getCount();
        }
    }


    private class BagIterator implements Iterator<T> {

        private final Iterator<Map.Entry<T, MutableInteger>> iterator;
        private Map.Entry<T, MutableInteger> currentEntry;
        private int currentEntryIndex;

        public BagIterator() {
            this.iterator = getMap().entrySet().iterator();
        }

        @Override
        public boolean hasNext() {
            if (currentEntry == null) {
                return iterator.hasNext();
            } else {
                return (currentEntryIndex + 1) < currentEntry.getValue().getCount() || iterator.hasNext();
            }
        }

        @Override
        public T next() {
            if (currentEntry == null) {
                return getEntryFromIterator();
            } else {
                currentEntryIndex++;
                if (currentEntryIndex == currentEntry.getValue().getCount()) {
                    currentEntryIndex = 0;
                    currentEntry = null;
                    return getEntryFromIterator();
                }
                return currentEntry.getKey();
            }
        }

        private T getEntryFromIterator() {
            final Map.Entry<T, MutableInteger> entry = iterator.next();
            if (entry.getValue().getCount() > 1) {
                currentEntryIndex = 0;
                currentEntry = entry;
            }
            return entry.getKey();
        }

        @Override
        public void remove() {
            if (currentEntry == null) {
                iterator.remove();
            } else {
                currentEntry.getValue().dec();
                currentEntryIndex--;
                if (currentEntry.getValue().getCount() == 0) {
                    iterator.remove();
                    currentEntry = null;
                    currentEntryIndex = 0;
                }
            }
            size--;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AbstractBag<?> bag = (AbstractBag<?>) o;
        return Objects.equals(getMap(), bag.getMap());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getMap());
    }

    @Override
    public String toString() {
        return "Bag{" +
                "map=" + getMap() +
                '}';
    }
}
