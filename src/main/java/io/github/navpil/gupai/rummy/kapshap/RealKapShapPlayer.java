package io.github.navpil.gupai.rummy.kapshap;

import io.github.navpil.gupai.util.ConsoleInput;
import io.github.navpil.gupai.CivilMilitaryComparator;
import io.github.navpil.gupai.Domino;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

public class RealKapShapPlayer implements KapShapPlayer {

    private final ConsoleInput consoleInput;
    private final String name;
    private final List<Domino> dominos = new ArrayList<>();

    private KapShapHand winningHand;

    public RealKapShapPlayer(String name) {
        consoleInput = new ConsoleInput();
        this.name = name;

    }
    @Override
    public void showTable(KapShapTableVisibleInformation table) {
        //no-op, real player sees everything anyway
    }

    @Override
    public void deal(List<Domino> dominoes) {
        this.dominos.clear();
        this.dominos.addAll(dominoes);
    }

    @Override
    public Domino offer(List<Domino> offer) {
        final StringBuilder sb = new StringBuilder();
        List<KapShapHand> possibleHands = getAllPossibleHands();
        possibleHands.forEach(hand -> sb.append("Possible hand: ").append(hand).append("\n"));
        sb.append("You're offered these dominoes, which will you take?\n")
                .append("0. None\n");
        for (int i = 0; i < offer.size(); i++) {
            sb.append(i + 1).append(". ").append(offer.get(i)).append("\n");
        }

        final String prompt = sb.toString();
        final int index = consoleInput.readInt(
                (i) -> i >= 0 && i <= offer.size(),
                prompt,
                "Invalid input"
        );
        if (index == 0) {
            return null;
        }
        return offer.get(index - 1);
    }

    @Override
    public void give(Domino domino) {
        System.out.println("You were dealt " + domino);
        this.dominos.add(domino);
        Collections.sort(dominos);
        final Collection<Ngan> ngans = listNgans(dominos);
        if (ngans.isEmpty()) {
            return;
        }
        for (Ngan ngan : ngans) {
            final KapShapHand hand = getHand(ngan, dominos);
            if (hand.winningHand()) {
                winningHand = hand;
                return;
            }
        }

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

    @Override
    public Domino getDiscard() {
        final StringBuilder sb = new StringBuilder();
        List<KapShapHand> possibleHands = getAllPossibleHands();
        possibleHands.forEach(hand -> sb.append("Possible hand: ").append(hand).append("\n"));
        sb.append("You have these dominoes, which will you discard?\n");
        for (int i = 0; i < dominos.size(); i++) {
            sb.append(i + 1).append(". ").append(dominos.get(i)).append("\n");
        }

        final String prompt = sb.toString();
        final int index = consoleInput.readInt(
                (i) -> i > 0 && i <= dominos.size(),
                prompt,
                "Invalid input"
        );
        final Domino domino = dominos.get(index - 1);
        dominos.remove(domino);
        return domino;
    }

    private List<KapShapHand> getAllPossibleHands() {
        Collections.sort(dominos);
        final Collection<Ngan> ngans = listNgans(dominos);
        if (ngans.isEmpty()) {
            return List.of(getHand(null, dominos));
        } else {
            return ngans.stream().map(ng -> getHand(ng, dominos)).collect(Collectors.toList());
        }
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
