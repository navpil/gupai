package io.github.navpil.gupai.mod10.daling;

import io.github.navpil.gupai.dominos.Domino;
import io.github.navpil.gupai.dominos.DominoUtil;
import io.github.navpil.gupai.mod10.Mod10Rule;

import java.util.Collection;
import java.util.List;

public class DaLingHand {

    private final List<Collection<Domino>> all;

    public DaLingHand(Domino single, Collection<Domino> pair, Collection<Domino> triplet) {
        all = List.of(List.of(single), pair, triplet);
    }

    @Override
    public String toString() {
        return "DaLingHand{" +
                "all=" + all +
                '}';
    }

    public boolean allMatchExactly(int bankerPoints) {
        return all.stream().allMatch(ds -> Mod10Rule.DA_LING.getPoints(ds).canBeExactly(bankerPoints));
    }

    public int getTotalPipCountFor(Table.GameColor color) {
        return all.stream().flatMap(Collection::stream).mapToInt(ds -> color == Table.GameColor.RED ? DominoUtil.redPointsCount(ds) : DominoUtil.blackPointsCount(ds)).sum();
    }

    public Collection<Domino> getStack(int i) {
        return all.get(i);
    }
}
