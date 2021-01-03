package io.navpil.github.zenzen.fishing.tsungshap;

import io.navpil.github.zenzen.dominos.Domino;
import io.navpil.github.zenzen.dominos.DominoUtil;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class TsungShapPlayer {

    private final String name;
    private TsungShapTable table;

    public TsungShapPlayer(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public TsungShapMove chooseMove(Domino domino, LinkedList<Domino> row) {
        TsungShapMove move = fetchMaxMove(domino, row);
        if (move == null) {
            return new TsungShapMove(TsungShapMove.Type.DISCARD, DominoUtil.getPipPoints(domino) > 7 ? TsungShapMove.Side.LEFT : TsungShapMove.Side.RIGHT, domino);
        }
        return move;
    }

    private TsungShapMove fetchMaxMove(Domino domino, LinkedList<Domino> row) {

        boolean pairWillSweep = row.size() == 1;
        boolean tripleWillSweep = row.size() == 2;
        final TsungShapRuleSet ruleSet = table.getRuleSet();

        final HashMap<TsungShapMove, Integer> modifiableMap = new HashMap<>();

        //Calculate triplets only if pairs can't sweep
        if (!pairWillSweep) {
            addEntry(modifiableMap, domino, tripleWillSweep, ruleSet, List.of(domino, row.getFirst(), row.getLast()), TsungShapMove.Side.BOTH, TsungShapMove.Type.TRIPLET);
            addEntry(modifiableMap, domino, tripleWillSweep, ruleSet, List.of(domino, row.getFirst(), row.get(1)), TsungShapMove.Side.LEFT, TsungShapMove.Type.TRIPLET);
            addEntry(modifiableMap, domino, tripleWillSweep, ruleSet, List.of(domino, row.getLast(), row.get(row.size() - 2)), TsungShapMove.Side.RIGHT, TsungShapMove.Type.TRIPLET);
        }

        addEntry(modifiableMap, domino, pairWillSweep, ruleSet, List.of(domino, row.getFirst()), TsungShapMove.Side.LEFT, TsungShapMove.Type.PAIR);
        addEntry(modifiableMap, domino, pairWillSweep, ruleSet, List.of(domino, row.getLast()), TsungShapMove.Side.RIGHT, TsungShapMove.Type.PAIR);

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

    public void showTable(TsungShapTable table) {
        this.table = table;
    }
}
