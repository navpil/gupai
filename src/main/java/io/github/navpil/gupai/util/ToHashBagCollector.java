package io.github.navpil.gupai.util;

import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;

public class ToHashBagCollector<T> implements Collector<T, HashBag<T>, HashBag<T>> {

    @Override
    public Supplier<HashBag<T>> supplier() {
        return HashBag::new;
    }

    @Override
    public BiConsumer<HashBag<T>, T> accumulator() {
        return AbstractBag::add;
    }

    @Override
    public BinaryOperator<HashBag<T>> combiner() {
        return (ts, ts2) -> {
            ts.addAll(ts2);
            return ts;
        };
    }

    @Override
    public Function<HashBag<T>, HashBag<T>> finisher() {
        return null;
    }

    @Override
    public Set<Characteristics> characteristics() {
        return Set.of(Characteristics.IDENTITY_FINISH, Characteristics.UNORDERED);
    }
}
