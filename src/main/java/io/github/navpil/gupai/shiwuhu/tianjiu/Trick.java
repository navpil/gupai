package io.github.navpil.gupai.shiwuhu.tianjiu;

import io.github.navpil.gupai.dominos.Domino;
import io.github.navpil.gupai.util.CombineCollection;
import io.github.navpil.gupai.util.ZippedCollection;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class Trick {

    private Collection<Domino> latestHighest;
    private List<Entry> beatEntries = new ArrayList<>();
    private List<Entry> discardEntries = new ArrayList<>();
    private final HandHelper handHelper;

    public Trick(int current, Collection<Domino> lead, RuleSet ruleSet) {
        this.latestHighest = lead;
        beatEntries.add(new Entry(current, lead));
        handHelper = new HandHelper(ruleSet);
    }

    public void discard(int current, Collection<Domino> discard) {
        if (discard.size() != latestHighest.size()) {
            throw new IllegalArgumentException("Cannot discard less/more than " + latestHighest.size());
        }
        discardEntries.add(new Entry(current, discard));
    }

    public void beat(int current, Collection<Domino> beat) {
        if (beat.size() != latestHighest.size()) {
            throw new IllegalArgumentException("Cannot beat less/more than " + latestHighest.size());
        }
        if (canBeat(beat, latestHighest)) {
            beatEntries.add(new Entry(current, beat));
            latestHighest = beat;
        } else {
            throw new IllegalArgumentException(beat + " cannot beat " + latestHighest);
        }
    }

    private boolean canBeat(Collection<Domino> beat, Collection<Domino> latestHighest) {
        return handHelper.canBeat(latestHighest, beat);
    }

    public int getTrickWinner() {
        return beatEntries.get(beatEntries.size() - 1).playerIndex;
    }

    public Collection<Domino> highest() {
        return latestHighest;
    }

    public int size() {
        return latestHighest.size();
    }

    public List<Domino> getAllDominos() {
        final List<Collection<Domino>> collect = new CombineCollection<>(List.of(beatEntries, discardEntries)).stream().map(Entry::getTiles).collect(Collectors.toList());
        return new ArrayList<>(new ZippedCollection<>(collect));
    }

    private static class Entry {

        private final int playerIndex;
        private final Collection<Domino> tiles;

        public Entry(int playerIndex, Collection<Domino> tiles) {
            this.playerIndex = playerIndex;
            this.tiles = tiles;
        }

        public Collection<Domino> getTiles() {
            return tiles;
        }
    }
}
