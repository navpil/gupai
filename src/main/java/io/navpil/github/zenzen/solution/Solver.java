package io.navpil.github.zenzen.solution;

import io.navpil.github.zenzen.BoardState;
import io.navpil.github.zenzen.Combination;
import io.navpil.github.zenzen.Move;
import io.navpil.github.zenzen.Triplet;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;

public class Solver {

    private final int maxNoValueAdded;
    private final Comparator<Move> moveComparator;
    private final Predicate<List<Triplet>> winningCondition;
    private final boolean rememberState = true;
    private final Set<BoardState> states = new HashSet<>();
    private boolean stopExecution = false;

    public Solver() {
        this(2);
    }

    public Solver(int maxNoValueAdded) {
        this(maxNoValueAdded, Move::compareTo, Solver::finished);
    }

    public Solver(int maxNoValueAdded, WinningCondition winningCondition) {
        this(maxNoValueAdded, winningCondition::compareMoves, winningCondition::hasWon);
    }

    private Solver(int maxNoValueAdded, Comparator<Move> moveComparator, Predicate<List<Triplet>> won) {
        this.maxNoValueAdded = maxNoValueAdded;
        this.moveComparator = moveComparator;
        this.winningCondition = won;
    }

    public Solution solve(List<Triplet> triplets) {
        return new Solution(makeNextMove(triplets, 0), states, triplets, maxNoValueAdded);
    }

    private List<Move> makeNextMove(List<Triplet> triplets, int noValueAdded) {
        if (shouldStop()) return null;
        List<Move> moves = getMoves(triplets);
        if (moves.size() == 0) {
            System.out.println("No more moves");
        } else {
            for (Move move : moves) {
                if (!move.addsValue() && noValueAdded >= maxNoValueAdded) {
                    continue;
                }
                move.execute();
                final int size = states.size();
                if (rememberState && !states.add(new BoardState(triplets))) {
                    move.undo();
                    continue;
                }
                if (size != states.size() && states.size() % 1000 == 1) {
                    System.out.println("Analyzed Board States: " + size + " and counting...");
                }
                if (winningCondition.test(triplets)) {
                    move.undoWithKeep();
                    return new ArrayList<>(Collections.singletonList(move));
                } else {
                    final List<Move> resultingMoves = makeNextMove(triplets, move.addsValue() ? 0 : noValueAdded + 1);
                    if (resultingMoves == null) {
                        move.undo();
                    } else if (resultingMoves != null) {
                        move.undoWithKeep();
                        resultingMoves.add(move);
                        return resultingMoves;
                    }
                }
            }
        }
        return null;
    }

    private boolean shouldStop() {
        if (Thread.interrupted()) {
            System.out.println("Interrupted");
            stopExecution = true;
            return true;
        }
        if (stopExecution) {
            return true;
        }
        return false;
    }

    private static boolean finished(List<Triplet> triplets) {
        for (Triplet triplet : triplets) {
            if (triplet.getCombination() == Combination.none) {
                return false;
            }
        }
        System.out.println("Won");
        System.out.println(triplets);
        return true;
    }


    private List<Move> getMoves(List<Triplet> triplets) {
        List<Move> moves = new ArrayList<>();
        for (int i = 0; i < triplets.size() - 1; i++) {
            final Triplet firstTriplet = triplets.get(i);
            for (int j = i + 1; j < triplets.size(); j++) {
                final Triplet secondTriplet = triplets.get(j);

                for (int k = 0; k < 3; k++) {
                    for (int l = 0; l < 3; l++) {
                        final Move move = new Move(firstTriplet, k, secondTriplet, l);
                        if (move.valid()) {
                            moves.add(move);
                        }
                    }
                }
            }
        }

        Collections.shuffle(moves);
        try {
            moves.sort(moveComparator);
        } catch (IllegalArgumentException e) {
            System.out.println("Could not sort moves: " + moves);
            throw e;
        }
        return moves;
    }

}
