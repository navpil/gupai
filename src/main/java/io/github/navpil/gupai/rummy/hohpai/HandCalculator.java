package io.github.navpil.gupai.rummy.hohpai;

import io.github.navpil.gupai.DominoUtil;
import io.github.navpil.gupai.XuanHePuPai;
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

    private final XuanHePuPai xuanHePuPai;
    private static final Set<XuanHePuPai.Combination> PAIRS = Set.of(
            XuanHePuPai.Combination.CIVIL_PAIR,
            XuanHePuPai.Combination.MILITARY_KOREAN_PAIR,
            XuanHePuPai.Combination.MILITARY_CHINESE_PAIR,
            XuanHePuPai.Combination.SUPREME_PAIR
    );
    private static final Set<XuanHePuPai.Combination> THREES = Set.of(
            XuanHePuPai.Combination.BIG_THREE,
            XuanHePuPai.Combination.SMALL_THREE
    );

    public HandCalculator(RuleSet ruleSet) {
        xuanHePuPai = XuanHePuPai.hoHpai(ruleSet.useSok());
    }

    public int calculatePoints(Hand winningHand) {
        // - 2 counts as 5
        // - 3 and 4 count as 4
        // - other counts as 3
        if (!winningHand.getDeadwood().isEmpty()) {
            return 0;
        }
        if (winningHand.getCombinations().size() == 1) {
            final Collection<Domino> straight = winningHand.getCombinations().iterator().next();
            if (xuanHePuPai.evaluate(straight) != XuanHePuPai.Combination.STRAIGHT) {
                return 0;
            }
            final Domino domino = straight.stream().filter(DominoUtil::isDouble).findAny().orElseThrow(() -> new IllegalStateException("Should have a double in straight, but it has none: " + straight));
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
            final XuanHePuPai.Combination firstCombination = xuanHePuPai.evaluate(combinations.get(0));
            final XuanHePuPai.Combination secondCombination = xuanHePuPai.evaluate(combinations.get(1));

            final HashBag<Domino> allTiles = new HashBag<>();
            allTiles.addAll(combinations.get(0));
            allTiles.addAll(combinations.get(1));

            //Sanity check
            if (firstCombination == XuanHePuPai.Combination.none || secondCombination == XuanHePuPai.Combination.none) {
                return 0;
            }
            //Calculate special cases first
            //Two dragons count as 3
            if (firstCombination == XuanHePuPai.Combination.DRAGON && secondCombination == XuanHePuPai.Combination.DRAGON) {
                final Optional<Domino> anyCivilTile = allTiles.stream().filter(Domino::isCivil).findAny();
                if (anyCivilTile.isPresent()) {
                    return 3;
                } else {
                    //Only military tiles;
                    return 5;
                }
            } else if (firstCombination == secondCombination) {
                if (firstCombination == XuanHePuPai.Combination.ER_SAN_KAO
                        || firstCombination == XuanHePuPai.Combination.SMALL_THREE
                        || firstCombination == XuanHePuPai.Combination.BIG_THREE
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
        } else if (winningHand.getCombinations().size() == 3) {
            //Pairs:
            for (Collection<Domino> pair : winningHand.getCombinations()) {
                //Technically they all should be valid pairs, otherwise they would not get here, but still let's check
                if (!PAIRS.contains(xuanHePuPai.evaluate(pair))) {
                    return 0;
                }
                return 4;
            }
        }
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
