package io.github.navpil.gupai.xiangshifu;

import io.github.navpil.gupai.IDomino;
import io.github.navpil.gupai.CombinationType;

/**
 * Represents a move
 *
 * Move takes two triplets and two indexes.
 */
public class Move implements Comparable<Move> {

    //First triplet with an index
    private final Triplet first;
    private final int index1;
    //Second triplet with an index
    private final Triplet second;
    private final int index2;

    //Copies of triplets (used in a toString() method)
    private Triplet firstCopy;
    private Triplet secondCopy;

    //Current combinations and pair potentials
    private final CombinationType combination1;
    private final CombinationType pairPotential1;
    private final CombinationType combination2;
    private final CombinationType pairPotential2;

    //Future combinations and pair potentials
    private final CombinationType futureCombination1;
    private final CombinationType futurePairPotential1;
    private final CombinationType futureCombination2;
    private final CombinationType futurePairPotential2;

    /**
     * Move contains two triplets and two indexes
     *
     * @param first first triplet
     * @param index1 first index
     * @param second second triplet
     * @param index2 second index
     */
    public Move(Triplet first, int index1, Triplet second, int index2) {
        this.first = first;
        this.index1 = index1;
        this.second = second;
        this.index2 = index2;

        firstCopy = first;
        secondCopy = second;

        combination1 = first.getCombination();
        pairPotential1 = first.getPairPotential();
        combination2 = second.getCombination();
        pairPotential2 = second.getPairPotential();

        final TripletEvaluation tuple = first.evaluateReplacement(second.peek(index2), index1);
        futureCombination1 = tuple.getCombination();
        futurePairPotential1 = tuple.getPairPotential();

        final TripletEvaluation tuple2 = second.evaluateReplacement(first.peek(index1), index2);
        futureCombination2 = tuple2.getCombination();
        futurePairPotential2 = tuple2.getPairPotential();

    }

    public CombinationType getCombination1() {
        return combination1;
    }

    public CombinationType getCombination2() {
        return combination2;
    }

    public CombinationType getFutureCombination1() {
        return futureCombination1;
    }

    public CombinationType getFutureCombination2() {
        return futureCombination2;
    }

    public CombinationType getPairPotential1() {
        return pairPotential1;
    }

    public CombinationType getPairPotential2() {
        return pairPotential2;
    }

    public CombinationType getFuturePairPotential1() {
        return futurePairPotential1;
    }

    public CombinationType getFuturePairPotential2() {
        return futurePairPotential2;
    }

    /**
     * Valid move should result in two valid future combinations
     * @return true if move is valid, false otherwise
     */
    public boolean valid() {
        return (!first.equals(second)) && (futureCombination1 != CombinationType.none && futureCombination2 != CombinationType.none);
    }

    /**
     * Move adds value when number of valid combination increases, or in other words, at least one of the original
     * combinations is none
     * @return true if move adds value, false otherwise
     */
    public boolean addsValue() {
        return combination1 == CombinationType.none || combination2 == CombinationType.none;
    }

    @Override
    public String toString() {
        return "Move{" +
                "" + firstCopy +
                "[" + index1 + ']' +
                " <> " + secondCopy +
                "[" + index2 +
                "]}" + " while was "
                + combination1 + " and " + combination2
                + " will result in "
                + futureCombination1
                + " and "
                + futureCombination2;

    }

    /**
     * Execute a move
     */
    public void execute() {
        final IDomino firstDomino = first.peek(index1);
        final IDomino secondDomino = second.peek(index2);

        first.replace(index1, secondDomino);
        second.replace(index2, firstDomino);
    }

    /**
     * Undo a move, but create a copy of both triplets, so toString() shows correct values.
     * This method is called when this move is a part of a solution
     */
    public void undoWithKeep() {
        final IDomino firstDomino = first.peek(index1);
        final IDomino secondDomino = second.peek(index2);

        first.replace(index1, secondDomino);
        second.replace(index2, firstDomino);

        firstCopy = new Triplet(first);
        secondCopy = new Triplet(second);
    }

    /**
     * Undo a move - this move does not bring us anywhere
     */
    public void undo() {
        final IDomino firstDomino = first.peek(index1);
        final IDomino secondDomino = second.peek(index2);

        first.replace(index1, secondDomino);
        second.replace(index2, firstDomino);
    }

    @Override
    public int compareTo(Move o) {
        final int thisMoveStart = (combination1 == null ? 1 : 0) + (combination2 == null ? 1 : 0);
        final int thatMoveStart = (o.combination1 == null ? 1 : 0) + (o.combination2 == null ? 1 : 0);
        return thatMoveStart - thisMoveStart;
    }
}
