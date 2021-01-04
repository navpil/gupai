package io.navpil.github.zenzen.fishing;

import io.navpil.github.zenzen.ChineseDominoSet;
import io.navpil.github.zenzen.dominos.Domino;
import io.navpil.github.zenzen.fishing.tsungshap.RunManySimulations;
import io.navpil.github.zenzen.jielong.Stats;
import io.navpil.github.zenzen.util.AbstractBag;
import io.navpil.github.zenzen.util.Bag;
import io.navpil.github.zenzen.util.TreeBag;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;

public class TiuU {

    public static void main(String[] args) {

        final List<Player> players = List.of(
                new ComputerPlayer("Comp-1"),
                new ComputerPlayer("Comp-2"),
                new RealPlayer("John")
        );
        final List<Domino> deck = ChineseDominoSet.create(2);
        final RuleSet rules = RuleSet.mixed();
        final int simCount = 10;

        Stats overallStats = new RunManySimulations().runManySimulations(deck, players, rules, simCount, TiuU::runSimulation);

        for (Player player : players) {
            System.out.println(player.getName() + " has " + overallStats.getPointsFor(player.getName()));
        }
    }

    public static Stats runSimulation(List<Domino> dominos, List<Player> players, RuleSet ruleSet, int whoGoesFirst) {
        int tilesPerPlayer = 24 / players.size();

        final Table table = new Table(ruleSet);

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

        final Stats stats = new Stats();

        for (Player player : players) {
            Bag<Domino> catches = table.getCatch(player.getName());
            if (catches.isEmpty()) {
                System.out.println("Player " + player.getName() + " caught nothing");
                continue;
            }
            final int points = table.getRuleSet().calculatePoints(catches);
            System.out.println(player.getName() + " caught " + catches + " which gives " + points + " points");
            stats.put(player.getName(), points);
        }
        return stats;
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
