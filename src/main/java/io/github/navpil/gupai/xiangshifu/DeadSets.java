package io.github.navpil.gupai.xiangshifu;

import io.github.navpil.gupai.Domino;
import io.github.navpil.gupai.IDomino;
import io.github.navpil.gupai.util.Bag;
import io.github.navpil.gupai.util.TreeBag;

import java.util.Collection;
import java.util.Set;

public class DeadSets {

    /*
     * [1:4][2:4][3:3], [1:4][2:4][4:5], [1:5][2:5][3:3], [1:5][2:5][4:4] and [2:3][2:4][3:4]
     */
    private static final Set<Bag<? extends IDomino>> deadGroups = Set.of(
            new TreeBag<>(Domino.ofList(14, 24, 33)),
            new TreeBag<>(Domino.ofList(14, 24, 45)),
            new TreeBag<>(Domino.ofList(15, 25, 33)),
            new TreeBag<>(Domino.ofList(15, 25, 44)),
            new TreeBag<>(Domino.ofList(23, 24, 34))
    );

    public boolean isDeadSet(Collection<? extends IDomino> dominos) {
        return deadGroups.contains(new TreeBag<>(dominos));
    }
}
