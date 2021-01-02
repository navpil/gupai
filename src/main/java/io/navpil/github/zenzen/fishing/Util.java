package io.navpil.github.zenzen.fishing;

import io.navpil.github.zenzen.dominos.Domino;
import io.navpil.github.zenzen.util.HashBag;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class Util {

    public static Set<Catch> allPossibleCatches(Collection<Domino> baits, Collection<Domino> pool, RuleSet ruleSet) {
        Set<Catch> catches = new HashSet<>();
        for (Domino bait : baits) {
            for (Domino fish : pool) {
                if (ruleSet.canCatch(bait, fish)) {
                    boolean canCatchThree = false;
                    final HashBag<Domino> triplet = HashBag.of(fish, fish, fish);
                    if (bait.equals(fish)) {
                        triplet.strictRetainAll(pool);
                        if (triplet.size() == 3 && ruleSet.tripleCatchAllowedFor(fish)) {
                            canCatchThree = true;
                        }
                    }
                    if (canCatchThree) {
                        catches.add(new Catch(bait, triplet));
                    } else {
                        catches.add(new Catch(bait, HashBag.of(fish)));
                    }
                }
            }
        }
        return catches;
    }

}
