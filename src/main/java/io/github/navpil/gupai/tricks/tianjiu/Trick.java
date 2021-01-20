package io.github.navpil.gupai.tricks.tianjiu;

import io.github.navpil.gupai.Domino;
import io.github.navpil.gupai.util.CombineCollection;
import io.github.navpil.gupai.util.ZippedCollection;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class Trick {

    /**
     * The highest cards yet put to the trick (those that should be beaten)
     */
    private Collection<Domino> latestHighest;

    /**
     * Entries which were placed face up
     */
    private List<Entry> beatEntries = new ArrayList<>();

    /**
     * Entries which were placed face down
     */
    private List<Entry> discardEntries = new ArrayList<>();
    private final HandHelper handHelper;

    /**
     * The trick starts with someone leading to it
     *
     * @param current player No
     * @param lead cards which are put
     * @param ruleSet rule set to use
     */
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

    /**
     * Helper class, records what cards were put by which player
     */
    private static class Entry {

        /**
         * Who played the cards
         */
        private final int playerIndex;
        /**
         * What cards were played
         */
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
