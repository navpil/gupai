package io.github.navpil.gupai.fishing.tsungshap;

import io.github.navpil.gupai.Domino;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class HandHelper {

    private final TsungShapRuleSet ruleSet;

    public HandHelper(TsungShapRuleSet ruleSet) {
        this.ruleSet = ruleSet;
    }

    public TsungShapMove fetchMaxMove(Domino domino, LinkedList<Domino> row) {
        return fetchMaxMove(List.of(domino), row);
    }
    public TsungShapMove fetchMaxMove(List<Domino> dominos, LinkedList<Domino> row) {

        boolean pairWillSweep = row.size() == 1;
        boolean tripleWillSweep = row.size() == 2;

        final HashMap<TsungShapMove, Integer> modifiableMap = new HashMap<>();

        //Calculate triplets only if pairs can't sweep
        for (Domino domino : dominos) {
            if (!pairWillSweep) {
                addEntry(modifiableMap, domino, tripleWillSweep, ruleSet, List.of(domino, row.getFirst(), row.getLast()), TsungShapMove.Side.BOTH, TsungShapMove.Type.TRIPLET);
                addEntry(modifiableMap, domino, tripleWillSweep, ruleSet, List.of(domino, row.getFirst(), row.get(1)), TsungShapMove.Side.LEFT, TsungShapMove.Type.TRIPLET);
                addEntry(modifiableMap, domino, tripleWillSweep, ruleSet, List.of(domino, row.getLast(), row.get(row.size() - 2)), TsungShapMove.Side.RIGHT, TsungShapMove.Type.TRIPLET);
            }

            addEntry(modifiableMap, domino, pairWillSweep, ruleSet, List.of(domino, row.getFirst()), TsungShapMove.Side.LEFT, TsungShapMove.Type.PAIR);
            addEntry(modifiableMap, domino, pairWillSweep, ruleSet, List.of(domino, row.getLast()), TsungShapMove.Side.RIGHT, TsungShapMove.Type.PAIR);
        }

        if (modifiableMap.isEmpty()) {
            return null;
        } else if (modifiableMap.size() == 1) {
            return modifiableMap.keySet().iterator().next();
        }

        int maxPoints = -1;
        TsungShapMove maxMove = null;

        for (Map.Entry<TsungShapMove, Integer> entry : modifiableMap.entrySet()) {
            if (entry.getValue() > maxPoints) {
                maxPoints = entry.getValue();
                maxMove = entry.getKey();
            }
        }

        return maxMove;
    }

    private void addEntry(HashMap<TsungShapMove, Integer> moveToPoints, Domino domino, boolean willSweep, TsungShapRuleSet ruleSet, List<Domino> dominoes, TsungShapMove.Side side, TsungShapMove.Type type) {
        if (type == TsungShapMove.Type.TRIPLET ? ruleSet.validTriplet(dominoes) : ruleSet.validPair(dominoes)) {
            final TsungShapCatch c = new TsungShapCatch(dominoes);
            moveToPoints.put(
                    new TsungShapMove(type, side, domino),
                    ruleSet.calculatePoints(List.of(willSweep ? c.asSweep() : c)));
        }
    }


    public boolean isValidMove(TsungShapMove tsungShapMove, LinkedList<Domino> row) {
        final TsungShapMove.Type type = tsungShapMove.getType();
        if (type == TsungShapMove.Type.DISCARD) {
            return true;
        }
        List<Domino> dominos = Collections.emptyList();
        if (type == TsungShapMove.Type.TRIPLET) {
            switch (tsungShapMove.getSide()) {
                case LEFT: dominos = List.of(tsungShapMove.getDomino(), row.getFirst(), row.get(1)); break;
                case RIGHT: dominos = List.of(tsungShapMove.getDomino(), row.get(row.size() - 2), row.getLast()); break;
                case BOTH: dominos = List.of(tsungShapMove.getDomino(), row.getFirst(), row.getLast()); break;
            }
        } else {
            switch (tsungShapMove.getSide()) {
                case LEFT: dominos = List.of(tsungShapMove.getDomino(), row.getFirst()); break;
                case RIGHT: dominos = List.of(tsungShapMove.getDomino(), row.getLast()); break;
            }
        }
        return type == TsungShapMove.Type.TRIPLET ? ruleSet.validTriplet(dominos) : ruleSet.validPair(dominos);
    }
}
