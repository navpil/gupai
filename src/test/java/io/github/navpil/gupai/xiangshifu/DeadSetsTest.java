package io.github.navpil.gupai.xiangshifu;

import io.github.navpil.gupai.ChineseDominoSet;
import io.github.navpil.gupai.CombinationType;
import io.github.navpil.gupai.Domino;
import io.github.navpil.gupai.XuanHePaiPu;
import io.github.navpil.gupai.util.CollectionUtil;
import io.github.navpil.gupai.util.HashBag;
import io.github.navpil.gupai.util.TreeBag;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

public class DeadSetsTest {

    @Test
    public void testAllDeadSets() {

        final XuanHePaiPu xuanHePaiPu = XuanHePaiPu.xiangShiFu();
        Set<TreeBag<Domino>> deads = getAllDeadTriplets(xuanHePaiPu);
        final DeadSets deadSets = new DeadSets();
        assertThat(deads.size()).isEqualTo(5);
        assertThat(deads.stream().allMatch(deadSets::isDeadSet)).isTrue();

        for (TreeBag<Domino> dead : deads) {
            if (deadSets.isDeadSet(dead)) {
                System.out.println("Set " + dead + " is officialy dead");
            } else {
                System.out.println("Set " + dead + " is possibly  dead");
            }
        }
    }

    @Test
    public void testNoDeadSetsForModernXiangShiFu() {

        Set<TreeBag<Domino>> deads = getAllDeadTriplets(XuanHePaiPu.xiangShiFuModern());

        assertThat(deads).isEmpty();

    }

    private Set<TreeBag<Domino>> getAllDeadTriplets(XuanHePaiPu xuanHePaiPu) {
        final List<Domino> dominos = ChineseDominoSet.create();
        final Collection<Collection<Domino>> permutations = CollectionUtil.allPermutations(dominos, 3);
        final Set<TreeBag<Domino>> allPossibleTriplets = permutations.stream().map(TreeBag::new).collect(Collectors.toSet());

        final HashBag<Domino> allDominoesBag = new HashBag<>(dominos);

        Set<TreeBag<Domino>> deads = new HashSet<>();

        for (TreeBag<Domino> triplet : allPossibleTriplets) {

            if (xuanHePaiPu.evaluate(triplet) != CombinationType.none) {
                continue;
            }
            allDominoesBag.strictRemoveAll(triplet);
            triplet_evalution:
            {
                final Collection<Collection<Domino>> pairs = CollectionUtil.allPermutations(triplet, 2);
                for (Collection<Domino> pair : pairs) {
                    for (Domino replacement : allDominoesBag) {
                        final ArrayList<Domino> possiblyValidTriplet = new ArrayList<>(pair);
                        possiblyValidTriplet.add(replacement);
                        if (xuanHePaiPu.evaluate(possiblyValidTriplet) != CombinationType.none) {
                            break triplet_evalution;
                        }
                    }
                }
                deads.add(triplet);
            }
            allDominoesBag.addAll(triplet);
        }
        return deads;
    }

}
