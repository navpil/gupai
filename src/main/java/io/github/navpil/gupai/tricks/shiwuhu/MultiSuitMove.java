package io.github.navpil.gupai.tricks.shiwuhu;

import io.github.navpil.gupai.Domino;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

/**
 * Multi suit move technically consists of several Single Suit moves combined.
 *
 * In order to beat a MultiSuit lead, all simple suit moves should beat lead Simple Suit moves
 *
 */
public class MultiSuitMove {

    private final HashMap<Suit, SingleSuitMove> simpleMovesBySuit;

    /**
     * Creates a move which consists of several dominos of several suits
     *
     * @param dominos dominos to create a trick with
     */
    public MultiSuitMove(List<Domino> dominos) {
        final HashMap<Suit, List<Domino>> dominoesBySuit = new HashMap<>();
        simpleMovesBySuit = new HashMap<>();
        for (Domino domino : dominos) {
            final Suit suit = Suit.findType(domino);
            if (!dominoesBySuit.containsKey(suit)) {
                dominoesBySuit.put(suit, new ArrayList<>());
            }
            dominoesBySuit.get(suit).add(domino);
        }

        for (Suit suit : dominoesBySuit.keySet()) {
            simpleMovesBySuit.put(suit, new SingleSuitMove(dominoesBySuit.get(suit)));
        }
    }

    /**
     * Checks whether this move can beat another move
     *
     * @param multiSuitMove move to beat
     * @return is higher or not
     */
    public boolean beats(MultiSuitMove multiSuitMove) {
        boolean beats = true;
        for (Suit suit : multiSuitMove.simpleMovesBySuit.keySet()) {
            final SingleSuitMove singleSuitMove = simpleMovesBySuit.get(suit);
            if (singleSuitMove == null || !singleSuitMove.beats(multiSuitMove.simpleMovesBySuit.get(suit))) {
                return false;
            }
        }
        return beats;
    }

    /**
     * Only extracts the cards needed to beat the trick.
     *
     * @param multiSuitMove move to beat
     * @return dominos required to beat the multisuite move
     */
    public List<Domino> extractBeat(MultiSuitMove multiSuitMove) {
        final ArrayList<Domino> result = new ArrayList<>();

        for (Suit suit : multiSuitMove.simpleMovesBySuit.keySet()) {
            final SingleSuitMove simpleLead = multiSuitMove.simpleMovesBySuit.get(suit);
            result.addAll(times(simpleLead.getSize(), simpleMovesBySuit.get(suit).getDomino()));

        }
        return result;
    }

    private List<Domino> times(int largestCounter, Domino largestDomino) {
        final ArrayList<Domino> result = new ArrayList<>();
        for (int i = 0; i < largestCounter; i++) {
            result.add(largestDomino);
        }
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MultiSuitMove that = (MultiSuitMove) o;
        return Objects.equals(simpleMovesBySuit, that.simpleMovesBySuit);
    }

    @Override
    public int hashCode() {
        return Objects.hash(simpleMovesBySuit);
    }

    @Override
    public String toString() {
        return "MultiSuitMove{" +
                "simpleMovesBySuit=" + simpleMovesBySuit +
                '}';
    }
}
