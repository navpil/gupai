package io.github.navpil.gupai.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class CollectionUtil {

    public static <T> Collection<Collection<T>> allPermutations(Collection<T> collection, int size) {
        return innerAllPermutations(new ArrayList<>(collection), size);
    }

    private static <T> Collection<Collection<T>> innerAllPermutations(List<T> ts, int size) {
        if (size == 0) {
            final ArrayList<T> objects = new ArrayList<>();
            final ArrayList<Collection<T>> metaCollection = new ArrayList<>();
            metaCollection.add(objects);
            return metaCollection;
        }
        final ArrayList<Collection<T>> permutations = new ArrayList<>();
        for (int i = 0; i < ts.size(); i++) {
            final T firstElement = ts.get(i);
            final Collection<Collection<T>> collections = innerAllPermutations(ts.subList(i + 1, ts.size()), size - 1);
            for (Collection<T> collection : collections) {
                collection.add(firstElement);
                permutations.add(collection);
            }
        }
        return permutations;
    }

}
