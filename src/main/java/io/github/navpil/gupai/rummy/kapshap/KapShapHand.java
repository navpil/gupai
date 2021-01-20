package io.github.navpil.gupai.rummy.kapshap;

import io.github.navpil.gupai.Domino;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class KapShapHand {

    /**
     * An "eye" - has to identical tiles
     */
    private final Ngan ngan;

    private final List<Mod10Pair> pairs;
    private final List<Domino> deadwood;

    public KapShapHand(Ngan ngan, List<Mod10Pair> pairs, List<Domino> deadwood) {
        this.ngan = ngan;
        this.pairs = new ArrayList<>(pairs);
        Collections.sort(this.pairs);
        this.deadwood = deadwood;
        Collections.sort(deadwood);
    }
    public KapShapHand(Ngan ngan, List<Mod10Pair> pairs) {
        this(ngan, pairs, Collections.emptyList());
    }

    public List<Domino> getDeadwood() {
        return deadwood;
    }

    public boolean winningHand() {
        return ngan != null && deadwood.isEmpty();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        KapShapHand that = (KapShapHand) o;
        return Objects.equals(ngan, that.ngan) &&
                Objects.equals(pairs, that.pairs) &&
                Objects.equals(deadwood, that.deadwood);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ngan, pairs, deadwood);
    }

    @Override
    public String toString() {
        return "KapShapHand{" +
                "ngan=" + ngan +
                ", pairs=" + pairs +
                ", deadwood=" + deadwood +
                '}';
    }
}
