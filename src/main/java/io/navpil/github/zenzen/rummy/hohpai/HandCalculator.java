package io.navpil.github.zenzen.rummy.hohpai;

import io.navpil.github.zenzen.dominos.Domino;
import io.navpil.github.zenzen.util.CollectionUtil;
import io.navpil.github.zenzen.util.HashBag;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

public class HandCalculator {

    private final RuleSet ruleSet;
    private final XuanHePuPai xuanHePuPai;

    public HandCalculator(RuleSet ruleSet) {
        xuanHePuPai = XuanHePuPai.hoHpai(ruleSet.useSok());
        this.ruleSet = ruleSet;
    }

    public int calculatePoints(Hand winningHand) {
        return 0;
    }

    public Collection<Hand> handPermutations(List<Domino> dominos) {
        final HashSet<Hand> hands = new HashSet<>();

        final XuanHePuPai.Combination combination = xuanHePuPai.evaluate(dominos);
        if (combination != XuanHePuPai.Combination.none) {
            hands.add(new Hand(List.of(dominos), Collections.emptyList()));
        }

        final Collection<Collection<Domino>> permutations = CollectionUtil.allPermutations(dominos, 3);

        for (Collection<Domino> permutation : permutations) {
            if (xuanHePuPai.evaluate(permutation) != XuanHePuPai.Combination.none) {
                final HashBag<Domino> otherTriplet = new HashBag<>(dominos);
                otherTriplet.strictRemoveAll(permutation);
                if (xuanHePuPai.evaluate(otherTriplet) != XuanHePuPai.Combination.none) {
                    hands.add(new Hand(List.of(permutation, otherTriplet), Collections.emptyList()));
                } else {
                    hands.add(new Hand(List.of(permutation), otherTriplet));
                }
            }
        }
        return hands;
    }
}
