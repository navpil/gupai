package io.github.navpil.gupai.util;

import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;

public class ToTreeBagCollector<T> implements Collector<T, TreeBag<T>, TreeBag<T>> {

    @Override
    public Supplier<TreeBag<T>> supplier() {
        return TreeBag::new;
    }

    @Override
    public BiConsumer<TreeBag<T>, T> accumulator() {
        return AbstractBag::add;
    }

    @Override
    public BinaryOperator<TreeBag<T>> combiner() {
        return (ts, ts2) -> {
            ts.addAll(ts2);
            return ts;
        };
    }

    @Override
    public Function<TreeBag<T>, TreeBag<T>> finisher() {
        return null;
    }

    @Override
    public Set<Characteristics> characteristics() {
        return Set.of(Characteristics.IDENTITY_FINISH, Characteristics.UNORDERED);
    }
}
