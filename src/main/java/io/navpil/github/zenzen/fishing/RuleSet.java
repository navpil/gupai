package io.navpil.github.zenzen.fishing;

import io.navpil.github.zenzen.dominos.Domino;
import io.navpil.github.zenzen.jielong.player.MutableInteger;
import io.navpil.github.zenzen.util.Bag;
import io.navpil.github.zenzen.util.HashBag;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

public class RuleSet {

    private static final Set<Domino> GEE_JOON = Set.of(Domino.of(2, 1), Domino.of(4, 2));

    //Positive Exceptions:

    //42 and 21 can catch each other
    private final boolean geeJoonMatch;

    //42, 21, 51, 33 can catch each other
    private final boolean geeJoonsMatchSixes;

    //11 and 66 can catch each other
    private final boolean heavenEarthMatch;

    //--Negative Exceptions:

    //55 and 64 cannot catch each other, even though they both score 10 points
    private final boolean plumsReadHeadDiffer;

    //Mixed pairs worth more points only if One Wen and both Wu are present
    //With the restriction [6:6],[6:3],[6:3] count as small fish (only one Wu is present)
    //Without the restriction [6:6],[6:3],[6:3] count as big fish (one Wu is enough)
    private final boolean oneWenTwoWuRestriction;

    //Can only be 64 (2x32) or 84 (4x21) set
    private final int deckSize;

    private final PointsCalculation pointsCalculation;

    //Bonus when all tiles of the Heaven-Nine are present
    private final int allTianJiuBonus;

    //Bonus when all 3 Jun (4x[5:5], 4x[6:5], 4x[6:4]) are present
    private final int allJunBonus;

    //Bonus when all supremes ([2:1] and [4:2]) are present
    private final int allSupremeBonus;

    public enum PointsCalculation {
        //According to Culin there are two kinds of fish - small and big
        CULIN,

        //According to the blogbost in the aloneinthefart there are three kinds of fish
        SHI_WU_HU
    }

    public RuleSet(boolean geeJoonMatch, boolean geeJoonsMatchSixes, boolean heavenEarthMatch, boolean plumsReadHeadDiffer, boolean oneWenTwoWuRestriction, int deckSize, PointsCalculation pointsCalculation, int allTianJiuBonus, int allJunBonus, int allSupremeBonus) {
        this.geeJoonMatch = geeJoonMatch;
        this.geeJoonsMatchSixes = geeJoonsMatchSixes;
        this.heavenEarthMatch = heavenEarthMatch;
        this.plumsReadHeadDiffer = plumsReadHeadDiffer;
        this.oneWenTwoWuRestriction = oneWenTwoWuRestriction;
        this.deckSize = deckSize;
        this.pointsCalculation = pointsCalculation;
        this.allTianJiuBonus = allTianJiuBonus;
        this.allJunBonus = allJunBonus;
        this.allSupremeBonus = allSupremeBonus;
    }

    /**
     * Creates rule set according to Culin's book. Supreme tiles can match each other, 2x32 deck, small fish &lt; 8 pips, only red pips count
     * @return rule set according to Culin
     */
    public static RuleSet culin() {
        return new RuleSet(true, false, false, false, false, 64, PointsCalculation.CULIN, 0, 0, 0);
    }

    /**
     * Creates rule set according to Alone in the Fart blog post.
     *
     * 64 and 55 cannot match each other; 84 deck; three kinds of fish - small, medium, large;
     * additional large bonuses for some cards combination (simplified version).
     *
     * @return rule set according to Alone in the Fart blog post
     */
    public static RuleSet alone() {
        return new RuleSet(false, false, false, true, true, 84, PointsCalculation.SHI_WU_HU, 300, 300, 300);
    }

    /**
     * Alone in the Fart rule set adjusted to 2x32 deck. One Wen + Two Wu restriction for large fish is removed;
     * bonuses are slightly adjusted.
     *
     * @return Alone in the Fart rule set adjusted to
     */
    public static RuleSet mixed() {
        return new RuleSet(false, false, false, true, false, 64, PointsCalculation.SHI_WU_HU, 300, 300, 200);
    }

    public boolean tripleCatchAllowedFor(Domino fish) {
        if (pointsCalculation == PointsCalculation.SHI_WU_HU) {
            return Set.of(66, 11, 56, 55, 64, 21).stream().map(Domino::of).collect(Collectors.toSet()).contains(fish);
        }
        return true;
    }

    public boolean canCatch(Domino bait, Domino fish) {
        final int baitPts = points(bait);
        final int fishPts = points(fish);
        if (baitPts == fishPts) {
            //Negative exception
            //[6:4] and [5:5] cannot match each other
            if (plumsReadHeadDiffer && baitPts == 10) {
                //There no other tens, except for 6:4 and 5:5, no need for explicit check
                return bait.equals(fish);
            }
            return true;
        }

        //Positive exceptions:

        //[6:6] and [1:1] can match each other
        if (heavenEarthMatch) {
            //Only earth has 2 pts and only heaven has 12 pts, no need for explicit equals
            if ((baitPts == 2 && fishPts == 12) || (baitPts == 12 && fishPts == 2)) {
                return true;
            }
        }
        //[2:1], [4:2], [3:3], [5:1] can all match each other
        if (geeJoonsMatchSixes) {
            //only [2:1] can have 3 pts and it has to match with any 6
            if ((baitPts == 3 && fishPts == 6) || (baitPts == 6 && fishPts == 3)) {
                return true;
            }
        }
        //[2:1], [4:2] can match each other, but [2:1] does not match other sixes
        if (geeJoonMatch) {
            if (GEE_JOON.contains(bait) && GEE_JOON.contains(fish)) {
                return true;
            }
        }

        return false;
    }

    private int points(Domino d) {
        return d.getPips()[0] + d.getPips()[1];
    }

    public int calculatePoints(Collection<Domino> dominos) {
        if (pointsCalculation == PointsCalculation.CULIN) {
            final int smallFish = dominos.stream().filter(d -> points(d) < 8).mapToInt(this::redPointsCount).reduce(Integer::sum).orElse(0);
            final int noOnes = (smallFish / 10) * 10;
            final int smallFishPoints = noOnes < smallFish ? noOnes + 10 : smallFish;

            final int bigFishPoints = dominos.stream().filter(d -> points(d) >= 8).mapToInt(d -> 2 * (d.getPips()[0] + d.getPips()[1])).reduce(Integer::sum).orElse(0);
            return smallFishPoints + bigFishPoints;
        } else {
            //Possible big fish
            final Set<Domino> tianJiuSet = Set.of(Domino.of(6, 6), Domino.of(6, 3), Domino.of(5, 4));
            Bag<Domino> tianJiu = new HashBag<>();
            final Set<Domino> supremeSet = Set.of(Domino.of(4, 2), Domino.of(2, 1));
            Bag<Domino> supremes = new HashBag<>();
            Domino plum = Domino.of(5,5);
            Bag<Domino> plumsJun = new HashBag<>();
            Domino axe = Domino.of(6,5);
            Bag<Domino> axesJun = new HashBag<>();
            Domino ten = Domino.of(6,4);
            Bag<Domino> tensJun = new HashBag<>();

            //Possible small fish
            final Set<Domino> earthEightSet = Set.of(Domino.of(1, 1), Domino.of(6, 2), Domino.of(5, 3));
            Bag<Domino> earthEight = new HashBag<>();
            final Set<Domino> manSevenSet = Set.of(Domino.of(4, 4), Domino.of(5, 2), Domino.of(4, 3));
            Bag<Domino> manSeven = new HashBag<>();
            final Set<Domino> gooseFiveSet = Set.of(Domino.of(3, 1), Domino.of(3, 2), Domino.of(4, 1));
            Bag<Domino> gooseFive = new HashBag<>();

            //Small fish
            MutableInteger smallFishCount = new MutableInteger();
            MutableInteger middleFishCount = new MutableInteger();
            MutableInteger bigFishCount = new MutableInteger();

            boolean allSupremes;
            boolean allJun;
            boolean allTianJiu;

            for (Domino domino : dominos) {
                if (tianJiuSet.contains(domino)) {
                    tianJiu.add(domino);
                } else if (supremeSet.contains(domino)) {
                    supremes.add(domino);
                } else if (plum.equals(domino)) {
                    plumsJun.add(domino);
                } else if (axe.equals(domino)) {
                    axesJun.add(domino);
                } else if (ten.equals(domino)) {
                    tensJun.add(domino);
                } else if (earthEightSet.contains(domino)) {
                    earthEight.add(domino);
                } else if (manSevenSet.contains(domino)) {
                    manSeven.add(domino);
                } else if (gooseFiveSet.contains(domino)) {
                    gooseFive.add(domino);
                } else {
                    smallFishCount.inc();
//                    smallFish.add(domino);
                }
            }

            strictComparison(supremes, supremeSet, bigFishCount, smallFishCount);
            allSupremes = deckSize == 64 ? supremes.size() == 4 : supremes.size()==8;

            //Jun
            MutableInteger junCount = new MutableInteger();
            getJunCount(plumsJun, bigFishCount, smallFishCount, junCount);
            getJunCount(axesJun, bigFishCount, smallFishCount, junCount);
            getJunCount(tensJun, bigFishCount, smallFishCount, junCount);
            allJun = junCount.getCount() == 3;

            //WenWu
            if (oneWenTwoWuRestriction) {
                strictComparison(tianJiu, tianJiuSet, bigFishCount, smallFishCount);
                strictComparison(earthEight, earthEightSet, middleFishCount, smallFishCount);
                strictComparison(manSeven, manSevenSet, middleFishCount, smallFishCount);
                strictComparison(gooseFive, gooseFiveSet, middleFishCount, smallFishCount);
            } else {
                lenientComparison(tianJiu, Domino.of(6, 6), Set.of(Domino.of(6, 3), Domino.of(5, 4)), bigFishCount, smallFishCount);
                lenientComparison(earthEight, Domino.of(1, 1), Set.of(Domino.of(5, 3), Domino.of(6, 2)), middleFishCount, smallFishCount);
                lenientComparison(manSeven, Domino.of(4, 4), Set.of(Domino.of(4, 3), Domino.of(5, 2)), middleFishCount, smallFishCount);
                lenientComparison(gooseFive, Domino.of(3, 1), Set.of(Domino.of(3, 2), Domino.of(4, 1)), middleFishCount, smallFishCount);
            }

            allTianJiu = tianJiu.size() == (deckSize == 64 ? 8 : 12);

            return smallFishCount.getCount() * 10 + middleFishCount.getCount() * 20 + bigFishCount.getCount() * 30 + (allSupremes ? allSupremeBonus : 0) + (allJun ? allJunBonus : 0) + (allTianJiu ? allTianJiuBonus : 0);
        }
    }

    private void lenientComparison(Bag<Domino> toCheck, Domino eye, Set<Domino> kickers, MutableInteger successCount, MutableInteger smallFishCount) {
        boolean containsAnyKicker = false;
        for (Domino kicker : kickers) {
            containsAnyKicker = containsAnyKicker || toCheck.contains(kicker);
        }
        if (containsAnyKicker && toCheck.contains(eye)) {
            successCount.add(toCheck.size());
        } else {
            smallFishCount.add(toCheck.size());
        }
    }

    private void strictComparison(Bag<Domino> toCheck, Set<Domino> expected, MutableInteger successCount, MutableInteger smallFishCount) {
        if (toCheck.containsAll(expected)) {
            successCount.add(toCheck.size());
        } else {
            smallFishCount.add(toCheck.size());
        }
    }

    private void getJunCount(Bag<Domino> jun, MutableInteger bigFishCount, MutableInteger smallFishCount, MutableInteger junCount) {
        if (jun.size() == 4) {
            bigFishCount.add(4);
            junCount.inc();
        } else {
            smallFishCount.add(jun.size());
        }
    }

    private int redPointsCount(Domino d) {
        final int[] pips = d.getPips();
        return (isRed(pips[0]) ? pips[0] : 0) + (isRed(pips[1]) ? pips[1] : 0);
    }

    private boolean isRed(int pip) {
        return pip == 1 || pip == 4;
    }

}
