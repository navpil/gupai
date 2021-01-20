package io.github.navpil.gupai.jielong.dingniu;

import io.github.navpil.gupai.Domino;
import io.github.navpil.gupai.jielong.Move;
import io.github.navpil.gupai.jielong.PipTracker;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Calculates the more advanced SuanZhang - so that some tiles are blocked out of the game.
 *
 * Examples:
 * <br/>
 * In the following game fours are out, since only tiles which have fours are [6:4] and they can't be played anymore.
 * <pre>[2:6][6:3][3:1][1:6][6:5][5:6][6:1]</pre>
 * Blocked doubles are not considered to be SuanZhang, so
 * <pre>[6:4][4:6]</pre>
 * is not a blocked game even though [4:4] can never be played
 * <br/>
 * Another example (blocked 5):
 * <pre>[6:3][3:1][1:6][6:5][5:6][6:1][1:3]</pre>
 * Fives cannot be played, because only [5:1] are left and they cannot be played (all ones are out)
 * <br/>
 * Classically only two GraphSuanZhang are valid: Fours and Fives (as described), but technically other tiles can also
 * be blocked. This is configurable.
 *
 */
public class GraphSuanZhang {

    private Map<Integer, Collection<Connection>> connections;
    private Map<Domino, Connection> reverseMap;
    private final PipTracker pipTracker = PipTracker.dingNiuPipTracker();


    private int side1 = -1;
    private int side2 = -1;
    /**
     * Should all pips be checked for GraphSuanZhang, or only the classical ones
     */
    private boolean all = false;

    public GraphSuanZhang() {
        reset();
    }

    public GraphSuanZhang(boolean all) {
        reset();
        all = true;
    }

    public enum Type {
        NONE, FIVES, FOURS, OTHER
    }

    public void reset() {
        side1 = -1;
        side2 = -1;
        //6:5 x 2
        //6:4 x 2
        //6:1 x 2
        //3:1 x 2
        //5:1 x 2
        connections = new HashMap<>();
        for (int i = 1; i <= 6; i++) {
            connections.put(i, new ArrayList<>());
        }
        reverseMap = new HashMap<>();
        for (Domino domino : List.of(Domino.of(6, 5), Domino.of(6, 4), Domino.of(6, 1), Domino.of(3, 1), Domino.of(5, 1))) {
            final Connection connection = new Connection(domino, 2);
            final int[] pips = domino.getPips();
            connections.get(pips[0]).add(connection);
            connections.get(pips[1]).add(connection);
            reverseMap.put(domino, connection);
        }
        //6:3 x 1
        //6:2 x 1
        for (Domino domino : List.of(Domino.of(6, 2), Domino.of(6, 3))) {
            final Connection connection = new Connection(domino, 1);
            final int[] pips = domino.getPips();
            connections.get(pips[0]).add(connection);
            connections.get(pips[1]).add(connection);
            reverseMap.put(domino, connection);
        }
        pipTracker.reset();
    }

    public Type willSuanZhang(Move move) {
        if (side1 == -1) {
            return Type.NONE;
        }
        if (move.getInwards() == move.getOutwards()) {
            return Type.NONE;
        }
        diminish(move);
        Type type;
        if (move.getSide() == 1) {
            type = checkForSuanZhang(side2, move.getOutwards());
        } else {
            type = checkForSuanZhang(side1, move.getOutwards());
        }
        increase(move);
        return type;
    }

    public Type executeMove(Move move) {
        if (side1 == -1) {
            side1 = move.getOutwards();
            side2 = move.getInwards();
            diminish(move);
            return Type.NONE;
        }
        if (move.getSide() < 1) {
            return Type.NONE;
        }
        if (move.getInwards() == move.getOutwards()) {
            return Type.NONE;
        }
        diminish(move);
        if (move.getSide() == 1) {
            if (move.getInwards() != side1) {
                throw new IllegalStateException("Cannot connect " + move.getInwards() + " to " + side1);
            }
            side1 = move.getOutwards();
        } else {
            if (move.getInwards() != side2) {
                throw new IllegalStateException("Cannot connect " + move.getInwards() + " to " + side2);
            }
            side2 = move.getOutwards();
        }
        return checkForSuanZhang(side2, side1);
    }

    private Type checkForSuanZhang(int side1, int side2) {
        if (all) {
            for (int i = 1; i <= 6; i++) {
                if (pipTracker.count(i) == 0 || isConnected(side1, i) || isConnected(side2, i)) {
                    //noop
                } else {
                    return Type.OTHER;
                }
            }
            return Type.NONE;
        } else {
            if (pipTracker.count(4) == 0 || isConnected(side1, 4) || isConnected(side2, 4)) {
                //noop
            } else {
                return Type.FOURS;
            }
            if (pipTracker.count(5) == 0 || isConnected(side1, 5) || isConnected(side2, 5)) {
                //noop
            } else {
                return Type.FIVES;
            }
            return Type.NONE;
        }
    }

    private boolean isConnected(int a, int b) {
        for (Connection value : reverseMap.values()) {
            value.unmark();
        }
        return innerIsConnected(a, b);
    }
    private boolean innerIsConnected(int a, int b) {
        final Collection<Connection> connections = this.connections.get(a);
        for (Connection connection : connections) {
            if (connection.isMarked() || connection.count < 1) {
                continue;
            }
            connection.mark();
            if (connection.contains(b)) {
                return true;
            }
            if (innerIsConnected(connection.other(a), b)) {
                return true;
            }
        }
        return false;
    }

    private void diminish(Move move) {
        pipTracker.diminish(move);
        final Connection connection = reverseMap.get(Domino.of(move.getInwards(), move.getOutwards()));
        if (connection != null) connection.count--;
    }

    private void increase(Move move) {
        final Connection connection = reverseMap.get(Domino.of(move.getInwards(), move.getOutwards()));
        if (connection != null) {

            connection.count++;
        }
        pipTracker.increase(move);
    }

    private static class Connection {
        private Domino domino;
        private int count;
        private boolean marked;
        private boolean [] reversePips;

        private Connection(Domino domino, int count) {
            this.domino = domino;
            this.count = count;
            reversePips = new boolean[7];
            reversePips[domino.getPips()[0]] = true;
            reversePips[domino.getPips()[1]] = true;
        }

        public void mark() {
            marked = true;
        }

        public void unmark() {
            marked = false;
        }

        public boolean isMarked() {
            return marked;
        }

        public boolean contains(int pip) {
            return reversePips[pip];
        }

        public int other(int a) {
            if (domino.getPips()[0] == a) {
                return domino.getPips()[1];
            }
            return domino.getPips()[0];
        }

        @Override
        public String toString() {
            return "Connection{" + domino +
                    ", " + count +
                    '}';
        }
    }

}
