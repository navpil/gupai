package io.navpil.github.zenzen.rummy.smallmahjong;

import io.navpil.github.zenzen.ChineseDominoSet;
import io.navpil.github.zenzen.dominos.Domino;
import io.navpil.github.zenzen.fishing.tsungshap.RunManySimulations;
import io.navpil.github.zenzen.jielong.Stats;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static io.navpil.github.zenzen.rummy.smallmahjong.HandCalculator.evaluate;

public class SmallMahJong {

    public static void main(String [] args) {
        //TODO: dmp 01.01.2021 Actual calculation of a discard for a computer player
        //Actually the game can last very few rounds - maybe the given Computer players are good enough already.
        final List<Player> players = List.of(
                new ComputerPlayer("Comp-1"),
                new ComputerPlayer("Comp-2"),
                new ComputerPlayer("Comp-3")
//                new RealPlayer("Jim")
        );

        final List<Domino> deck = ChineseDominoSet.create();
        new RunManySimulations().runManySimulations(deck, players, "NoRules", 100, (dominos, players1, ruleSet, whoGoesFirst) -> Simulation.runSimulation(dominos, players1, whoGoesFirst));
//        calculateProbabilities();
    }

    private static void winProb() {
        final List<Domino> shuffledSet = ChineseDominoSet.create();
        int winningCount = 0;
        final int size = 10000;
        for (int i = 0; i < size; i++) {
            Collections.shuffle(shuffledSet);
            final ArrayList<Domino> hand = new ArrayList<>(shuffledSet.subList(0, 6));
            final List<Hand> hands = HandCalculator.calculateHands(hand);
            final Optional<Hand> any = hands.stream().filter(Hand::isWinning).findAny();
            if (any.isPresent()) {
                winningCount++;
            }
        }

        System.out.println("Probability of winning on deal is " + 100.0 * winningCount / size + "%");
    }

    public static void calculateProbabilities() {
        final List<Domino> dominos = ChineseDominoSet.create();
        final int size = dominos.size();


        int counter = 0;
        Map<TripletType, Set<List<Domino>>> allTypes = new HashMap<>();
        allTypes.put(TripletType.MIXED, new HashSet<>());
        allTypes.put(TripletType.SETS, new HashSet<>());
        allTypes.put(TripletType.STRAIGHTS, new HashSet<>());
        Set<List<Domino>> all = new HashSet<>();

        Map<Domino, Set<Triplet>> tripletsPerDomino = new HashMap<>();

        for (Domino domino : dominos) {
            tripletsPerDomino.put(domino, new HashSet<>());
        }

        System.out.println("--- All combinations follow ---");
        for (int i = 0; i < size; i++) {
            for (int j = i + 1; j < size; j++) {
                for (int k = j + 1; k < size; k++) {
                    final List<Domino> triplet = List.of(dominos.get(i), dominos.get(j), dominos.get(k));
                    final TripletType evaluate = evaluate(triplet);
                    if (evaluate != null) {
                        for (Domino domino : triplet) {
                            tripletsPerDomino.get(domino).add(new Triplet(triplet));
                        }
                        allTypes.get(evaluate).add(triplet);
                        all.add(triplet);
                        counter++;
                        System.out.println("Type " + evaluate + triplet);
                    }
                }
            }
        }

        System.out.println("Total combinations count: " + counter + ", unique " + all.size());
        for (Map.Entry<TripletType, Set<List<Domino>>> entry : allTypes.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue().size());
            for (List<Domino> dominoList : entry.getValue()) {
                System.out.println(dominoList);
            }
        }

        System.out.println("---Value for each domino may be calculated by how many triplet it is contained in---");
        Domino lastDomino = null;
        for (int i = 0; i < dominos.size(); i++) {
            final Domino domino = dominos.get(i);
            if (domino.equals(lastDomino)) {
                continue;
            }
            lastDomino = domino;
            System.out.println("Domino " + domino + " is contained in " + tripletsPerDomino.get(lastDomino).size());
        }

        System.out.println("--- Intersections for dominoes ---");

        for (int i = 0; i < size; i++) {
            for (int j = i + 1; j < size; j++) {
                final Domino d1 = dominos.get(i);
                final Domino d2 = dominos.get(j);

                final Set<Triplet> firstTriplets = tripletsPerDomino.get(d1);
                final Set<Triplet> secondTriplets = tripletsPerDomino.get(d2);

                Set<Triplet> intersection = new HashSet<>(firstTriplets);
                intersection.retainAll(secondTriplets);

                if (d1.equals(d2)) {
                    intersection = intersection.stream().filter(triplet -> triplet.containsBoth(d1, d2)).collect(Collectors.toSet());
                }

                System.out.println(d1 + ", " + d2 + ": " + intersection);

            }
        }
    }




}

