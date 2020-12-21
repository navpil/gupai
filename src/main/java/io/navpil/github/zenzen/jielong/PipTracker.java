package io.navpil.github.zenzen.jielong;

public class PipTracker {

    private int [] pips = {-1, 6, 1, 3, 2, 4, 8};

    public void reset() {
        pips = new int[] {-1, 6, 1, 3, 2, 4, 8};
    }

    public void diminish(Move move) {
        if (move.getOutwards() != move.getInwards()) {
            pips[move.getInwards()]--;
            pips[move.getOutwards()]--;
        }
    }

    public int count(int index) {
        return pips[index];
    }

    public void increase(Move move) {
        if (move.getOutwards() != move.getInwards()) {
            pips[move.getInwards()]++;
            pips[move.getOutwards()]++;
        }
    }
}
