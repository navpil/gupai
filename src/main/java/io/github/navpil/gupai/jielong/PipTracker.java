package io.github.navpil.gupai.jielong;

import io.github.navpil.gupai.DominoUtil;
import io.github.navpil.gupai.ChineseDominoSet;
import io.github.navpil.gupai.Domino;

import java.util.Collection;

/**
 * Tracks how many pips are left
 */
public class PipTracker {

    private final int [] pips = new int[7];
    private final int[] originalPipCount;

    private PipTracker(int[] originalPipCount) {
        this.originalPipCount = originalPipCount;
        System.arraycopy(this.originalPipCount, 0, pips, 0, 7);
    }

    public static PipTracker forSet(Collection<Domino> dominoSet) {
        int [] originalPipCount = new int[]{-1, 0, 0, 0, 0, 0, 0};
        for (Domino domino : dominoSet) {
            if (!DominoUtil.isDouble(domino)) {
                originalPipCount[domino.getPips()[0]]++;
                originalPipCount[domino.getPips()[1]]++;

            }
        }
        return new PipTracker(originalPipCount);
    }

    public static PipTracker dingNiuPipTracker() {
        return forSet(ChineseDominoSet.dingNiuSet());
    }

    public static PipTracker copyOf(PipTracker pipTracker) {
        return new PipTracker(pipTracker.originalPipCount);
    }

    public void reset() {
        System.arraycopy(originalPipCount, 0, pips, 0, 7);
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
