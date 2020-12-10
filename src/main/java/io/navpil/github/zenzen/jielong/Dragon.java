package io.navpil.github.zenzen.jielong;

import java.util.LinkedList;
import java.util.List;

public class Dragon {

    private final LinkedList<Integer> dragon = new LinkedList<>();
    private final LinkedList<Pair> pairs = new LinkedList<Pair>();
    private final OpenArms openArms;

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

    public enum OpenArms {
        SINGLE, DOUBLE;
    }

    public Dragon(Move firstMove, OpenArms openArms) {
        this.openArms = openArms;
        dragon.add(firstMove.getInwards());
        dragon.add(firstMove.getOutwards());
        pairs.add(new Pair(firstMove.getInwards(), firstMove.getOutwards()));
    }

    /*
     * From left to right, left is first, right is last
     *
     * X (closed)       [6:2][2:3][3:5] ... O (side = 1)
     * O (side = 2) ... [6:2][2:3][3:5] ... O (side = 1)
     *
     */
    public List<Integer> getOpenEnds() {
        if (openArms == OpenArms.SINGLE) {
            return List.of(dragon.getLast());
        } else {
            return List.of(dragon.getLast(), dragon.getFirst());
        }
    }

    public void executeMove(Move move) {
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

    @Override
    public String toString() {
        return "Dragon " + pairs;
    }
}
