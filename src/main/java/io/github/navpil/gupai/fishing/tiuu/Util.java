package io.github.navpil.gupai.fishing.tiuu;

import io.github.navpil.gupai.Domino;
import io.github.navpil.gupai.util.HashBag;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class Util {

    public static Set<Catch> allPossibleCatches(Collection<Domino> baits, Collection<Domino> pool, RuleSet ruleSet) {
        Set<Catch> catches = new HashSet<>();
        for (Domino bait : baits) {
            for (Domino fish : pool) {
                if (ruleSet.canCatch(bait, fish)) {
                    //Allow to catch one fish or three fish, there is no ability to catch 2 fish
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
