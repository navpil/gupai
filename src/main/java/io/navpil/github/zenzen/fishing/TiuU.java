package io.navpil.github.zenzen.fishing;

import io.navpil.github.zenzen.ChineseDominoSet;
import io.navpil.github.zenzen.dominos.Domino;
import io.navpil.github.zenzen.util.AbstractBag;
import io.navpil.github.zenzen.util.Bag;
import io.navpil.github.zenzen.util.HashBag;
import io.navpil.github.zenzen.util.TreeBag;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;

public class TiuU {

    public static void main(String[] args) {
        runSimulation(
                List.of(
                        new ComputerPlayer("Comp-1"),
                        new ComputerPlayer("Comp-2"),
                        new ComputerPlayer("Comp-3")
                ), 0
        );
    }

    public static void runSimulation(List<Player> players, int whoGoesFirst) {

        final List<Domino> dominos = ChineseDominoSet.create(2);
        Collections.shuffle(dominos);

        int tilesPerPlayer = 24 / players.size();

        final Table table = new Table(RuleSet.mixed());

        int counter = 0;
        for (Player player : players) {
            final List<Domino> deal = dominos.subList(counter * tilesPerPlayer, (counter + 1) * tilesPerPlayer);
            System.out.println("Player " + player.getName() + " was dealt " + deal);
            player.deal(deal);
            player.showTable(table);
            counter++;
        }

        final ArrayList<Domino> woodPile = new ArrayList<>(dominos.subList(counter * tilesPerPlayer, counter * tilesPerPlayer + 24));
        System.out.println("Wood pile " + woodPile);

        table.setupPool(dominos.subList((counter) * tilesPerPlayer + 24, dominos.size()));

        final CircularInteger playerIndex = new CircularInteger(players.size(), whoGoesFirst);
        for (int woodPileIndex = 0; woodPileIndex < 24; woodPileIndex++) {
            System.out.println("Index: " + woodPileIndex);
            System.out.println("Table pool: " + table.getPool());
            final Player player = players.get(playerIndex.current());
            System.out.println(player);
            Catch withOwn = player.fish(table.getPool());
            System.out.println(player.getName() + " catches " + withOwn);
            table.apply(player.getName(), withOwn);

            final Domino domino = woodPile.get(woodPileIndex);
            Catch fromWoodPile = player.fish(table.getPool(), domino);
            System.out.println(player.getName() + " catches " + fromWoodPile);
            table.apply(player.getName(), fromWoodPile);

            playerIndex.next();
        }

        Collection<Domino> all = new HashBag<>();
        for (Player player : players) {
            Bag<Catch> catches = table.getCatch(player.getName());
            if (catches == null) {
                System.out.println("Player " + player.getName() + " caught nothing");
                continue;
            }
            final Collection<Domino> collect = catches.stream().flatMap(c -> {
                final HashBag<Domino> d = new HashBag<>();
                d.add(c.getBait());
                d.addAll(c.getFish());
                return d.stream();
            }).collect(new ToBagCollector<>());
            all.addAll(collect);
            System.out.println(player.getName() + " caught " + collect + " which gives " + table.getRuleSet().calculatePoints(collect) + " points");
        }
        all.addAll(table.getPool());


    }

    public static class ToBagCollector<T> implements Collector<T, TreeBag<T>, TreeBag<T>> {

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


}
