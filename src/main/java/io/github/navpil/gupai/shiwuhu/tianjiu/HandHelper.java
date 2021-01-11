package io.github.navpil.gupai.shiwuhu.tianjiu;

import io.github.navpil.gupai.dominos.Domino;
import io.github.navpil.gupai.rummy.CivilMilitaryComparator;
import io.github.navpil.gupai.util.CollectionUtil;
import io.github.navpil.gupai.util.HashBag;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class HandHelper {

    private final RuleSet ruleSet;
    public static final List<Domino> ONE_TWO_THREE = Domino.ofList(11, 22, 33);
    public static final List<Domino> FOUR_FIVE_SIX = Domino.ofList(44, 55, 66);

    private static final Set<Domino> HEAVEN_NINE = new HashSet<>(Domino.ofList(66, 63, 54));
    private static final Set<Domino> EARTH_EIGHT = new HashSet<>(Domino.ofList(11, 62, 53));
    private static final Set<Domino> MAN_SEVEN = new HashSet<>(Domino.ofList(44, 52, 43));
    private static final Set<Domino> GOOSE_FIVE = new HashSet<>(Domino.ofList(31, 41, 32));

    private static final Set<Domino> SUPREME = new HashSet<>(Domino.ofList(42, 21));

    private static final Set<Set<Domino>> SPECIALS = Set.of(
            HEAVEN_NINE, EARTH_EIGHT, MAN_SEVEN, GOOSE_FIVE, SUPREME
    );

    private static final Map<Domino, Integer> RANKING = new HashMap<>();

    static {
                RANKING.put(Domino.of(66), 11);
                RANKING.put(Domino.of(11), 10);
                RANKING.put(Domino.of(44), 9);
                RANKING.put(Domino.of(31), 8);
                RANKING.put(Domino.of(55), 7);
                RANKING.put(Domino.of(33), 6);
                RANKING.put(Domino.of(22), 5);
                RANKING.put(Domino.of(65), 4);
                RANKING.put(Domino.of(64), 3);
                RANKING.put(Domino.of(61), 2);
                RANKING.put(Domino.of(51), 1);

                RANKING.put(Domino.of(63), 9);
                RANKING.put(Domino.of(54), 9);
                RANKING.put(Domino.of(62), 8);
                RANKING.put(Domino.of(53), 8);
                RANKING.put(Domino.of(52), 7);
                RANKING.put(Domino.of(43), 7);
                RANKING.put(Domino.of(42), 6);
                RANKING.put(Domino.of(41), 5);
                RANKING.put(Domino.of(32), 5);
                RANKING.put(Domino.of(21), 3);
    }

    public HandHelper(RuleSet ruleSet) {
        this.ruleSet = ruleSet;
    }

    public List<Collection<Domino>> calculateCombinations(List<Domino> dominos) {
        final ArrayList<Collection<Domino>> moves = new ArrayList<>();
        if (ruleSet.isAllow123()) {
            final HashBag<Domino> copy = new HashBag<>(dominos);
            if (copy.containsAll(ONE_TWO_THREE)) {
                moves.add(ONE_TWO_THREE);
            }
            if (copy.containsAll(FOUR_FIVE_SIX)) {
                moves.add(FOUR_FIVE_SIX);
            }
        }
        if (ruleSet.getTrickType() == RuleSet.TrickType.GU_PAI) {
            for (Domino domino : dominos) {
                moves.add(List.of(domino));
            }
            return moves;
        } else {
            Map<Set<Domino>, List<Domino>> specialCombinations = new HashMap<>();
            Map<Domino, List<Domino>> civils = new HashMap<>();
            for (Domino domino : dominos) {
                boolean specialFound = false;
                special_loop:
                for (Set<Domino> special : SPECIALS) {
                    if (special.contains(domino)) {
                        if (!specialCombinations.containsKey(special)) {
                            specialCombinations.put(special, new ArrayList<>());
                        }
                        specialCombinations.get(special).add(domino);
                        specialFound = true;
                        break special_loop;
                    }
                }
                if (!specialFound) {
                    if (!civils.containsKey(domino)) {
                        civils.put(domino, new ArrayList<>());
                    }
                    civils.get(domino).add(domino);
                }
            }
            moves.addAll(civils.values());
            if (ruleSet.getTrickType() == RuleSet.TrickType.TIAN_JIU) {
                moves.addAll(specialCombinations.values());
            } else if (ruleSet.getTrickType() == RuleSet.TrickType.TIAN_JIU_ONLY_PAIRS) {
                moves.addAll(specialCombinations.values().stream().flatMap(list -> splitToTwo(list).stream()).collect(Collectors.toList()));
            } else if (ruleSet.getTrickType() == RuleSet.TrickType.TIAN_JIU_NO_MIXING) {
                moves.addAll(specialCombinations.values().stream().flatMap(list -> splitToTwoNoMixed(list).stream()).collect(Collectors.toList()));
            }
        }

        return moves;
    }

    private Collection<Collection<Domino>> splitToTwo(Collection<Domino> list) {
        if (list.size() <= 2) {
            return List.of(list);
        }
        return CollectionUtil.allPermutations(list, 2);
    }

    private Collection<Collection<Domino>> splitToTwoNoMixed(Collection<Domino> list) {
        if (list.size() == 1) {
            return List.of(list);
        }
        HashBag<Domino> military = new HashBag<>();
        HashBag<Domino> civil = new HashBag<>();
        for (Domino domino : list) {
            if (domino.isCivil()) {
                civil.add(domino);
            } else {
                military.add(domino);
            }
        }
        return List.of(military, civil).stream().filter(b -> !b.isEmpty()).collect(Collectors.toList());
    }

    public boolean canBeat(Collection<Domino> lead, Collection<Domino> beat) {
        if (beat.size() != lead.size()) {
            return false;
        }
        if (isSupreme(lead) || isSupreme(beat)) {
            return false;
        }
        final ArrayList<Domino> leadDominos = new ArrayList<>(lead);
        leadDominos.sort(CivilMilitaryComparator.INSTANCE);

        final ArrayList<Domino> beatDominos = new ArrayList<>(beat);
        beatDominos.sort(CivilMilitaryComparator.INSTANCE);
        for (int i = 0; i < leadDominos.size(); i++) {
            final Domino lower = leadDominos.get(i);
            final Domino upper = beatDominos.get(i);

            if (lower.isCivil() && upper.isMilitary()) {
                return false;
            } else if (upper.isCivil() && lower.isMilitary()) {
                return false;
            }

            if (!canBeat(upper, lower)) {
                return false;
            }
        }
        return true;
    }

    private boolean isSupreme(Collection<Domino> lead) {
        return lead.containsAll(SUPREME);
    }

    private boolean canBeat(Domino upper, Domino lower) {
        return RANKING.get(upper) > RANKING.get(lower);
    }

    public boolean isValidHand(List<Domino> hand) {
        if (hand.isEmpty()) {
            return false;
        } if (hand.size() == 1) {
            return true;
        } else {
            final HashSet<Domino> handSet = new HashSet<>(hand);
            if (handSet.size() == 1) {
                //Equal tiles, which makes a civil pair
                return true;
            }
            for (Set<Domino> special : SPECIALS) {
                if (special.containsAll(handSet)) {
                    return true;
                }
            }
        }
        return false;
    }
}
