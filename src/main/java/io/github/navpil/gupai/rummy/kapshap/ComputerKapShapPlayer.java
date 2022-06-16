package io.github.navpil.gupai.rummy.kapshap;

import io.github.navpil.gupai.Domino;
import io.github.navpil.gupai.jielong.player.evaluators.PriorityUtil;
import io.github.navpil.gupai.CivilMilitaryComparator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

public class ComputerKapShapPlayer implements KapShapPlayer {

    private final String name;
    private List<Domino> dominoes;
    private KapShapHand winningHand;
    private Domino discard;
    private KapShapTableVisibleInformation table;

    public ComputerKapShapPlayer(String name) {
        this.name = name;
    }

    @Override
    public void showTable(KapShapTableVisibleInformation table) {
        this.table = table;
    }

    @Override
    public void deal(List<Domino> dominoes) {
        this.dominoes = new ArrayList<>(dominoes);
        winningHand = null;
        discard = null;
    }

    @Override
    public Domino offer(List<Domino> offer) {
        System.out.println("Offered: " + offer + " and I have " + dominoes);
        final KapShapTableVisibleInformation table = KapShapTableVisibleInformation.copyOf(this.table);
        int maxPriority = -1;
        KapShapHand bestHand = null;
        Domino wished = null;
        int maxAllowedDeadWood = dominoes.size() + 1;

        best_hand_loop:
        for (Domino domino : offer) {
            final ArrayList<Domino> dominoesCopy = new ArrayList<>(dominoes);
            dominoesCopy.add(domino);
            table.remove(domino);
            final KapShapHand goodHand = getBestHand(dominoesCopy, table);
            if (goodHand.winningHand()) {
                return domino;
            }

            final List<Integer> priorities;
            if (goodHand.getDeadwood().isEmpty()) {
                if (maxAllowedDeadWood > 2) {
                    maxAllowedDeadWood = 2;

                    //Reset
                    maxPriority = -1;
                    bestHand = null;
                    wished = null;
                }
                List<Integer> tempPriorities = getPriorities(dominoes, table, dominoes);
                Collections.sort(tempPriorities);
                //Two lowest priorities, since others belong to the pairs
                priorities = List.of(tempPriorities.get(0), tempPriorities.get(1));
            } else {
                if (goodHand.getDeadwood().size() > maxAllowedDeadWood) {
                    table.add(domino);
                    continue best_hand_loop;
                } else if (goodHand.getDeadwood().size() < maxAllowedDeadWood) {
                    maxAllowedDeadWood = goodHand.getDeadwood().size();

                    //Reset
                    maxPriority = -1;
                    bestHand = null;
                    wished = null;
                }

                priorities = getPriorities(goodHand.getDeadwood(), table, dominoesCopy);
            }
            //If there is no deadwood, then this should have been a winning hand
            //noinspection OptionalGetWithoutIsPresent
            final Integer minPriority = priorities.stream().reduce(Math::min).get();
            final int currentDeadwoodPriority = priorities.stream().reduce(Integer::sum).get() - minPriority;

            //Take the highest priority hand minus the one, which will be removed
            if (currentDeadwoodPriority > maxPriority) {
                maxPriority = currentDeadwoodPriority;
                bestHand = goodHand;
                wished = domino;
            }
            dominoesCopy.remove(domino);
            table.add(domino);
        }
        if (bestHand == null) {
            return null;
        }
        if (wished == null) {
            return null;
        }

        final KapShapTableVisibleInformation possibleTable = KapShapTableVisibleInformation.copyOf(this.table);
        possibleTable.remove(wished);
        ArrayList<Domino> possibleOwnDominoes = new ArrayList<>(dominoes);
        possibleOwnDominoes.add(wished);
        Domino discarded;
        if (bestHand.getDeadwood().isEmpty()) {
            //It may happen that hand consists only of pairs
            discarded = findDeadliestDomino(possibleOwnDominoes, possibleTable, possibleOwnDominoes);
        } else {
            discarded = findDeadliestDomino(bestHand.getDeadwood(), possibleTable, possibleOwnDominoes);
        }

        //No point in taking a domino if it will be discarded later
        if (discarded.equals(wished)) {
            return null;
        }
        return wished;
    }

    @Override
    public void give(Domino domino) {
        discard = null;
        final Domino discarded;
        dominoes.add(domino);
        if (dominoes.size() % 2 != 0) {
            throw new IllegalStateException("Adding a tile should always result in an even number of tiles");
        }
        Collections.sort(dominoes);
        final KapShapHand bestHand1 = getBestHand(dominoes, table);
        if (bestHand1.winningHand()) {
            winningHand = bestHand1;
            return;
        }
        if (bestHand1.getDeadwood().isEmpty()) {
            //It may happen that hand consists only of pairs
            discarded = findDeadliestDomino(dominoes, table, dominoes);
        } else {
            discarded = findDeadliestDomino(bestHand1.getDeadwood(), table, dominoes);
        }

        discard = discarded;
        dominoes.remove(discarded);
    }

    private static KapShapHand getBestHand(List<Domino> dominoes, KapShapTableVisibleInformation table) {
        Collection<Ngan> ngans = listNgans(dominoes);
        if (ngans.isEmpty()) {
            //Calculate a tile to discard
            return getHand(null, dominoes);

        } else {
            List<KapShapHand> hands = new ArrayList<>();
            for (Ngan ngan : ngans) {
                KapShapHand hand = getHand(ngan, dominoes);
                if (hand.winningHand()) {
                    return hand;
                }
                hands.add(hand);
            }
            int minDeadWood = Integer.MAX_VALUE;
            for (KapShapHand hand : hands) {
                minDeadWood = Math.min(hand.getDeadwood().size(), minDeadWood);
            }
            //Only finals can go into lambdas
            final int mDeadWood = minDeadWood;
            final List<KapShapHand> goodHands = hands.stream().filter(h -> h.getDeadwood().size() == mDeadWood).collect(Collectors.toList());

            KapShapHand bestHand;
            if (goodHands.size() > 1) {
                //Choose best hand
                int maxPriority = 0;
                bestHand = null;
                for (KapShapHand goodHand : goodHands) {
                    final List<Integer> priorities = getPriorities(goodHand.getDeadwood(), table, dominoes);
                    //If there is no deadwood, then this should have been a winning hand
                    //noinspection OptionalGetWithoutIsPresent
                    final Integer minPriority = priorities.stream().reduce(Math::min).get();
                    final int currentDeadwoodPriority = priorities.stream().reduce(Integer::sum).get() - minPriority;

                    //Take the highest priority hand minus the one, which will be removed
                    if (currentDeadwoodPriority > maxPriority) {
                        maxPriority = currentDeadwoodPriority;
                        bestHand = goodHand;
                    }
                }
            } else {
                bestHand = goodHands.get(0);
            }
            return bestHand;
        }
    }

    @Override
    public Domino getDiscard() {
        if (winningHand != null) {
            throw new IllegalStateException("Hand has won, cannot discard a tile");
        }
        if (discard == null) {
            throw new IllegalStateException("Discard was not calculated, cannot return anything");
        }
        return discard;
    }

    private Domino findDeadliestDomino(List<Domino> deadwood, KapShapTableVisibleInformation table, List<Domino> ownDominos) {
        Domino discarded;
        //First sort by Civil, civil go last (so discarded later)
        deadwood.sort(CivilMilitaryComparator.INSTANCE.reversed());

        List<Integer> priorities = getPriorities(deadwood, table, ownDominos);

        final List<Domino> sortedDeadwood = PriorityUtil.sort(priorities, deadwood);
        discarded = sortedDeadwood.get(0);
        return discarded;
    }

    private static List<Integer> getPriorities(List<Domino> deadwood,
                                               KapShapTableVisibleInformation table,
                                               List<Domino> dominoes) {
        int [] frequencies = table.getTotalFrequencies();

        Collection<Domino> openDominoes = table.getOpenDominoes();
        int[] openFrequencies = new int[10];
        if (table.getRules().getOfferType() == KapShapRuleset.Offer.ALL) {
            for (Domino openDomino : openDominoes) {
                openFrequencies[Mod10Calculation.mod10(openDomino)]++;
            }
        } else {
            for (Domino openDomino : openDominoes) {
                openFrequencies[Mod10Calculation.mod10(openDomino)]--;
            }

        }

        for (Domino myDomino : dominoes) {
            frequencies[Mod10Calculation.mod10(myDomino)]--;
        }
        List<Integer> priorities = new ArrayList<>();
        for (Domino dead : deadwood) {
            final int mod10 = Mod10Calculation.mod10(dead);
            int index;
            if (mod10 == 5 || mod10 == 0) {
                index = mod10;
            } else {
                index = 10 - mod10;
            }
            priorities.add(openFrequencies[index] + frequencies[index]);
        }
        return priorities;
    }

    private static KapShapHand getHand(Ngan ngan, List<Domino> dominoes) {
        if (ngan != null) {
            dominoes.remove(ngan.getDomino());
            dominoes.remove(ngan.getDomino());
        }

        List<Domino> deadwood = new ArrayList<>();
        final HashMap<Integer, List<Domino>> mod10Map = new HashMap<>();
        for (int i = 0; i < 10; i++) {
            mod10Map.put(i, new ArrayList<>());
        }
        for (Domino d : dominoes) {
            mod10Map.get(Mod10Calculation.mod10(d)).add(d);
        }

        List<Mod10Pair> pairs = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            //Use civil for pairs first
            mod10Map.get(i).sort(CivilMilitaryComparator.INSTANCE);
        }

        //Self pairing dominoes
        for (Integer sm : List.of(0, 5)) {
            final List<Domino> selfMod10 = mod10Map.get(sm);
            for (int i = 1; i < selfMod10.size(); i = i + 2) {
                pairs.add(new Mod10Pair(selfMod10.get(i), selfMod10.get(i - 1)));
            }
            if (selfMod10.size() % 2 == 1) {
                //One leftover
                deadwood.add(selfMod10.get(selfMod10.size() - 1));
            }
        }
        for (int i = 1; i < 5; i++) {
            final List<Domino> smaller = mod10Map.get(i);
            final List<Domino> bigger = mod10Map.get(10 - i);
            int maxIndex = Math.min(smaller.size(), bigger.size());
            for (int j = 0; j < maxIndex; j++) {
                pairs.add(new Mod10Pair(smaller.get(j), bigger.get(j)));
            }
            if (smaller.size() > bigger.size()) {
                for (int j = bigger.size(); j < smaller.size(); j++) {
                    deadwood.add(smaller.get(j));
                }
            } else if (bigger.size() > smaller.size()) {
                for (int j = smaller.size(); j < bigger.size(); j++) {
                    deadwood.add(bigger.get(j));
                }
            }
        }

        if (ngan != null) {
            dominoes.add(ngan.getDomino());
            dominoes.add(ngan.getDomino());
        }

        return new KapShapHand(ngan, pairs, deadwood);
    }

    private static Collection<Ngan> listNgans(List<Domino> dominoes) {
        Collection<Ngan> ngans = new HashSet<>();
        for (int i = 1; i < dominoes.size(); i++) {
            if (dominoes.get(i).equals(dominoes.get(i - 1))) {
                ngans.add(new Ngan(dominoes.get(i)));
            }
        }
        return ngans;
    }


    @Override
    public boolean hasWon() {
        return winningHand != null;
    }

    @Override
    public KapShapHand getWinningHand() {
        return winningHand;
    }


    @Override
    public String getName() {
        return name;
    }
}
