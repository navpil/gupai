package io.github.navpil.gupai.jielong;

import io.github.navpil.gupai.ChineseDominoSet;
import io.github.navpil.gupai.Domino;
import io.github.navpil.gupai.jielong.dingniu.SuanZhang;
import io.github.navpil.gupai.jielong.dingniu.SuanZhangImpl;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Represents tiles which are played on the table.
 */
public class Dragon {

    private final LinkedList<Integer> dragon = new LinkedList<>();
    private final LinkedList<Pair> pairs = new LinkedList<Pair>();
    private final OpenArms openArms;
    private final SuanZhang suanZhang;
    private final PipTracker pipTracker;

    private static class Pair {
        private final int left;
        private final int right;


        private Pair(int left, int right) {
            this.left = left;
            this.right = right;
        }

        @Override
        public String toString() {
            return "[" + left + ':' + right + ']';
        }
    }

    /**
     * Should it be a single-armed Dragon, so that one end is closed (such as in CeDeng),
     * or double-armed (such as in DingNiu)
     */
    public enum OpenArms {
        SINGLE, DOUBLE;
    }

    /**
     * Creates a Dragon used in DingNiu game
     *
     * @return DingNiu dragon
     */
    public static Dragon dingNiuDragon() {
        return new Dragon(OpenArms.DOUBLE, ChineseDominoSet.dingNiuSet(), new SuanZhangImpl());
    }

    public Dragon(OpenArms openArms, Collection<Domino> dominoSet, SuanZhang suanZhang) {
        this.openArms = openArms;
        this.suanZhang = suanZhang;
        pipTracker = PipTracker.forSet(dominoSet);
    }

    /*
     * From left to right, left is first, right is last
     *
     * X (closed)       [6:2][2:3][3:5] ... O (side = 1)
     * O (side = 2) ... [6:2][2:3][3:5] ... O (side = 1)
     *
     */
    public List<Integer> getOpenEnds() {
        if (dragon.isEmpty()) {
            return Collections.emptyList();
        }
        if (openArms == OpenArms.SINGLE) {
            return List.of(dragon.getLast());
        } else {
            return List.of(dragon.getLast(), dragon.getFirst());
        }
    }

    /**
     * Executes a move on the dragon
     *
     * @param move move to execute
     */
    public void executeMove(Move move) {
        suanZhang.executeMove(move);
        pipTracker.diminish(move);

        if (move.getSide() > 0) {
            if (move.getSide() == 1) {
                checkValid(dragon.getLast(), move.getInwards());
                dragon.add(move.getInwards());
                dragon.add(move.getOutwards());
                pairs.add(new Pair(move.getInwards(), move.getOutwards()));
            } else if (move.getSide() == 2) {
                checkDouble();
                checkValid(dragon.getFirst(), move.getInwards());
                dragon.addFirst(move.getInwards());
                dragon.addFirst(move.getOutwards());
                pairs.addFirst(new Pair(move.getOutwards(), move.getInwards()));
            }
        } else if (move.getSide() == 0){
            dragon.add(move.getInwards());
            dragon.add(move.getOutwards());
            pairs.add(new Pair(move.getInwards(), move.getOutwards()));
        }
    }

    private void checkDouble() {
        if (openArms != OpenArms.DOUBLE) {
            throw new IllegalStateException("Move is illegal! Cannot connect to the closed end");
        }
    }

    private void checkValid(Integer openEnd, int inwards) {
        if (inwards != openEnd) {
            throw new IllegalStateException("Move is illegal! Can not put " + inwards + " to " + openEnd);
        }
    }

    public PipTracker getPipTracker() {
        return pipTracker;
    }

    public SuanZhang suanZhang() {
        return suanZhang;
    }

    @Override
    public String toString() {
        return "Dragon " + pairs;
    }
}
