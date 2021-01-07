package io.github.navpil.gupai;

import io.github.navpil.gupai.dominos.IDomino;

public class Move implements Comparable<Move> {

    private final Triplet first;
    private final int index1;
    private final Triplet second;
    private final int index2;

    private Triplet firstCopy;
    private Triplet secondCopy;

    private final Combination combination1;
    private final Combination pairPotential1;
    private final Combination combination2;
    private final Combination pairPotential2;
    private final Combination futureCombination1;
    private final Combination futurePairPotential1;
    private final Combination futureCombination2;
    private final Combination futurePairPotential2;

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
        final Tuple tuple = first.getCombination(second.peek(index2), index1);
        futureCombination1 = tuple.getCombination();
        futurePairPotential1 = tuple.getPairPotential();
        final Tuple tuple2 = second.getCombination(first.peek(index1), index2);
        futureCombination2 = tuple2.getCombination();
        futurePairPotential2 = tuple2.getPairPotential();

    }

    public Combination getCombination1() {
        return combination1;
    }

    public Combination getCombination2() {
        return combination2;
    }

    public Combination getFutureCombination1() {
        return futureCombination1;
    }

    public Combination getFutureCombination2() {
        return futureCombination2;
    }

    public Combination getPairPotential1() {
        return pairPotential1;
    }

    public Combination getPairPotential2() {
        return pairPotential2;
    }

    public Combination getFuturePairPotential1() {
        return futurePairPotential1;
    }

    public Combination getFuturePairPotential2() {
        return futurePairPotential2;
    }

    public boolean valid() {
        return (!first.equals(second)) && (futureCombination1 != Combination.none && futureCombination2 != Combination.none);
    }

    public boolean addsValue() {
        return combination1 == Combination.none || combination2 == Combination.none;
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

    public void execute() {
        final IDomino firstDomino = first.peek(index1);
        final IDomino secondDomino = second.peek(index2);

        first.replace(index1, secondDomino);
        second.replace(index2, firstDomino);
    }

    public void undoWithKeep() {
        final IDomino firstDomino = first.peek(index1);
        final IDomino secondDomino = second.peek(index2);

        first.replace(index1, secondDomino);
        second.replace(index2, firstDomino);

        firstCopy = new Triplet(first);
        secondCopy = new Triplet(second);
    }

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
