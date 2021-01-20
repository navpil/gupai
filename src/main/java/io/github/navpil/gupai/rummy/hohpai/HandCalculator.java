package io.github.navpil.gupai.rummy.hohpai;

import io.github.navpil.gupai.CombinationType;
import io.github.navpil.gupai.DominoUtil;
import io.github.navpil.gupai.XuanHePaiPu;
import io.github.navpil.gupai.util.CollectionUtil;
import io.github.navpil.gupai.Domino;
import io.github.navpil.gupai.util.HashBag;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public class HandCalculator {

    private final XuanHePaiPu xuanHePaiPu;

    //1-2-3 and 4-5-6
    private static final Set<CombinationType> THREES = Set.of(
            CombinationType.BIG_THREE,
            CombinationType.SMALL_THREE
    );

    public HandCalculator(RuleSet ruleSet) {
        xuanHePaiPu = ruleSet.getXuanHePaiPu();
    }

    public int calculatePoints(Hand winningHand) {
        // - 2 counts as 5
        // - 3 and 4 count as 4
        // - other counts as 3
        if (!winningHand.getDeadwood().isEmpty()) {
            return 0;
        }
        if (winningHand.getCombinations().size() == 1) {
            final Collection<Domino> sixDominoesCombo = winningHand.getCombinations().iterator().next();
            final CombinationType combinationType = xuanHePaiPu.evaluate(sixDominoesCombo);
            if (combinationType == CombinationType.THREE_PAIRS) {
                return 4;
            }
            if (combinationType != CombinationType.STRAIGHT) {
                return 0;
            }
            final Domino domino = sixDominoesCombo.stream().filter(DominoUtil::isDouble).findAny().orElseThrow(() -> new IllegalStateException("Should have a double in straight, but it has none: " + sixDominoesCombo));
            switch (domino.getPips()[0]) {
                case 2:
                    return 5;
                case 3:
                case 4:
                    return 4;
                default:
                    return 3;
            }
        } else if (winningHand.getCombinations().size() == 2) {

            final ArrayList<? extends Collection<Domino>> combinations = new ArrayList<>(winningHand.getCombinations());
            final CombinationType firstCombination = xuanHePaiPu.evaluate(combinations.get(0));
            final CombinationType secondCombination = xuanHePaiPu.evaluate(combinations.get(1));

            final HashBag<Domino> allTiles = new HashBag<>();
            allTiles.addAll(combinations.get(0));
            allTiles.addAll(combinations.get(1));

            //Sanity check
            if (firstCombination == CombinationType.none || secondCombination == CombinationType.none) {
                return 0;
            }
            //Calculate special cases first
            //Two dragons count as 3
            if (firstCombination == CombinationType.DRAGON && secondCombination == CombinationType.DRAGON) {
                final Optional<Domino> anyCivilTile = allTiles.stream().filter(Domino::isCivil).findAny();
                if (anyCivilTile.isPresent()) {
                    return 3;
                } else {
                    //Only military tiles;
                    return 5;
                }
            } else if (firstCombination == secondCombination) {
                if (firstCombination == CombinationType.ER_SAN_KAO
                        || firstCombination == CombinationType.SMALL_THREE
                        || firstCombination == CombinationType.BIG_THREE
                ) {
                    //According to Culin only doubles count
                    if (allTiles.stream().allMatch(DominoUtil::isDouble)) {
                        return 3;
                    }
                }
            } else if (THREES.contains(firstCombination) && THREES.contains(secondCombination)) {
                //According to Culin only non-doubles count...
                if (allTiles.stream().noneMatch(DominoUtil::isDouble)){
                    return 3;
                }
                //...however this is a bit strange, so here is an only doubles variation:
                if (allTiles.stream().allMatch(DominoUtil::isDouble)){
                    return 3;
                }
            }
            //Most general case
            return 2;
        }
        return 0;
    }

    public Collection<Hand> handPermutations(List<Domino> dominos) {
        final HashSet<Hand> hands = new HashSet<>();

        final CombinationType combination = xuanHePaiPu.evaluate(dominos);
        if (combination != CombinationType.none) {
            hands.add(new Hand(List.of(dominos), Collections.emptyList()));
        }

        final Collection<Collection<Domino>> permutations = CollectionUtil.allPermutations(dominos, 3);

        for (Collection<Domino> permutation : permutations) {
            if (xuanHePaiPu.evaluate(permutation) != CombinationType.none) {
                final HashBag<Domino> otherTriplet = new HashBag<>(dominos);
                otherTriplet.strictRemoveAll(permutation);
                if (xuanHePaiPu.evaluate(otherTriplet) != CombinationType.none) {
                    hands.add(new Hand(List.of(permutation, otherTriplet), Collections.emptyList()));
                } else {
                    hands.add(new Hand(List.of(permutation), otherTriplet));
                }
            }
        }
        return hands;
    }

}
