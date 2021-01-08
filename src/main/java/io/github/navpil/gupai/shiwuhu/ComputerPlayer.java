package io.github.navpil.gupai.shiwuhu;

import io.github.navpil.gupai.dominos.Domino;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Describes a computer player who behaves depending on a passed strategy
 */
public class ComputerPlayer implements Player {

    public static final Domino HE = new Domino(1, 3);
    public static final Domino REN = new Domino(4, 4);
    public static final Domino DI = new Domino(1, 1);
    public static final Domino TIAN = new Domino(6, 6);

    /**
     * Contains all supremes, which were dealt (in case user had both 42 and 21) and also all taken tricks
     */
    private ArrayList<Domino> hu;
    private List<Domino> dominosToLead;
    private List<Domino> dominosToBeat;
    private final String name;
    private final Strategy strategy;

    private enum MoveType {
        LEAD, BEAT
    }

    public static class Strategy {

        //Should we first lead high, or should we first lead low, if true [6:6] wins over [1:1]
        private final boolean leadHighFirst;

        //If both multi-suit move and  single-suit moves have the same size - whether multi-suit move should have preference
        //if true [1:1][5:3] wins over [6:6][6:6], but not over [6:6][6:6][6:6]
        private final boolean leadCombinationFirstIfTie;

        //Regardless of a single-suit move size, if a multi-suit move is available - go with it.
        //if true [1:1][5:3] wins over [6:6][6:6][6:6][6:6]
        private final boolean alwaysLeadCombinationFirst;

        //Whether combination is broad will be more important of whether combination is high.
        //if true [1:1][1:1][1:1] wins over [3:3][3:3] if 'leadBroadFirst' is set to true
        private final boolean broadOverHigh;

        //Broader combination wins over narrower
        //if true [1:1][1:1][1:1] will win over [3:3][3:3] if broadOverhigh is set to true
        private final boolean leadBroadFirst;

        //Should the trick be beaten by the highest or lowest possible combination
        private final boolean beatHigh;

        public Strategy(boolean leadBroadFirst, boolean leadHighFirst, boolean leadCombinationFirstIfTie, boolean alwaysLeadCombinationFirst, boolean beatHigh, boolean broadOverHigh) {
            this.leadHighFirst = leadHighFirst;
            this.leadCombinationFirstIfTie = leadCombinationFirstIfTie;
            this.alwaysLeadCombinationFirst = alwaysLeadCombinationFirst;
            this.beatHigh = beatHigh;

            this.leadBroadFirst = leadBroadFirst;
            this.broadOverHigh = broadOverHigh;
        }

        public static Strategy createDefault() {
            return new Strategy(true, true, false, false, false, false);
        }
    }

    public ComputerPlayer(String name, Strategy strategy) {
        this.name = name;
        this.strategy = strategy;
    }

    @Override
    public void deal(Collection<Domino> dominos) {
        this.dominosToLead = new ArrayList<>(dominos);
        this.dominosToBeat = new ArrayList<>(dominos);
        if (strategy.leadHighFirst) {
            this.dominosToLead.sort(new ShiWuHuComparator());
        } else {
            this.dominosToLead.sort(new ShiWuHuComparator().reversed());
        }
        if (strategy.beatHigh) {
            this.dominosToBeat.sort(new ShiWuHuComparator());
        } else {
            this.dominosToBeat.sort(new ShiWuHuComparator().reversed());
        }

        hu = new ArrayList<>();

        if (hasBothSupremes(dominos)) {
            final List<Domino> supremesSet = Domino.ofList(42, 21);
            hu.addAll(dominos.stream().filter(supremesSet::contains).collect(Collectors.toList()));
            removeAll(hu);
        }
    }

    private void removeAll(Collection<Domino> dominos) {
        for (Domino domino : dominos) {
            dominosToBeat.remove(domino);
            dominosToLead.remove(domino);
        }
    }

    @Override
    public List<Domino> lead() {
        if (dominosToLead.isEmpty()) {
            return Collections.emptyList();
        }
        List<List<Domino>> combos = findAllComboMoves(MoveType.LEAD);

        //Combos are already sorted by being high, need to resort only if broad should take preference
        if (strategy.broadOverHigh) {
            if (strategy.leadBroadFirst) {
//                combos.sort(Comparator.comparingInt(List::size).reversed());
                combos.sort((l1, l2) -> l2.size() - l1.size());
            } else {
                combos.sort(Comparator.comparingInt(List::size));
            }
        }

        final List<Domino> combomove = combos.isEmpty() ? Collections.emptyList() : combos.get(0);
        if (strategy.alwaysLeadCombinationFirst && !combomove.isEmpty()) {
            removeAll(combomove);
            return combomove;
        }

        final List<SingleSuitMove> singleSuitMoves = findAllSimpleMoves(dominosToLead);

        if (strategy.broadOverHigh) {
            if (strategy.leadBroadFirst) {
                singleSuitMoves.sort(Comparator.comparingInt(SingleSuitMove::getSize).reversed());
            } else {
                singleSuitMoves.sort(Comparator.comparingInt(SingleSuitMove::getSize));
            }
        }

        final SingleSuitMove singleSuitMove2 = singleSuitMoves.get(0);
        List<Domino> simpleMove = times(singleSuitMove2.getSize(), singleSuitMove2.getDomino());

        if (simpleMove.size() == combomove.size()) {
            if (strategy.leadCombinationFirstIfTie) {
                removeAll(combomove);
                return combomove;
            } else {
                removeAll(simpleMove);
                return simpleMove;
            }
        }
        if (simpleMove.size() > combomove.size()) {
            removeAll(simpleMove);
            return simpleMove;
        } else {
            removeAll(combomove);
            return combomove;
        }
    }

    private List<List<Domino>> findAllComboMoves(MoveType moveType) {
        boolean highFirst = moveType == MoveType.LEAD ? strategy.leadHighFirst : strategy.beatHigh;

        List<Domino> tianJiu = fetch(dominosToLead, Set.of(TIAN, new Domino(6,3), new Domino(5,4)));
        List<Domino> diBa = fetch(dominosToLead, Set.of(DI, new Domino(5,3), new Domino(2,6)));
        List<Domino> renQi = fetch(dominosToLead, Set.of(REN, new Domino(5,2), new Domino(3,4)));
        List<Domino> heWu = fetch(dominosToLead, Set.of(HE, new Domino(3,2), new Domino(4,1)));

        List<List<Domino>> combos = new ArrayList<>();
        if (highFirst) {
            combos.add(getLargeComboMove(tianJiu, TIAN));
            combos.add(getLargeComboMove(diBa, DI));
            combos.add(getLargeComboMove(renQi, REN));
            combos.add(getLargeComboMove(heWu, HE));
        } else {
            combos.add(getLargeComboMove(heWu, HE));
            combos.add(getLargeComboMove(renQi, REN));
            combos.add(getLargeComboMove(diBa, DI));
            combos.add(getLargeComboMove(tianJiu, TIAN));
        }
        return combos;
    }

    private List<SingleSuitMove> findAllSimpleMoves(List<Domino> dominos) {
        final ArrayList<SingleSuitMove> result = new ArrayList<>();
        Domino d = dominos.get(0);
        int counter = 1;
        for (int i = 1; i < dominos.size(); i++) {
            final Domino next = dominos.get(i);
            if (d.equals(next)) {
                counter++;
            } else {
                result.add(new SingleSuitMove(times(counter, d)));
                d = next;
                counter = 1;
            }
        }
        result.add(new SingleSuitMove(times(counter, d)));
        return result;
    }

    private List<Domino> times(int largestCounter, Domino largestDomino) {
        final ArrayList<Domino> result = new ArrayList<>();
        for (int i = 0; i < largestCounter; i++) {
            result.add(largestDomino);
        }
        return result;
    }

    private List<Domino> getLargeComboMove(List<Domino> combo, Domino glueCard) {
        if (combo.contains(glueCard) && new HashSet<>(combo).size() > 1) {
            return new ArrayList<>(combo);
        } else {
            return Collections.emptyList();
        }
    }

    @Override
    public List<Domino> beat(List<Domino> lead) {
        if (dominosToBeat.isEmpty()) {
            return Collections.emptyList();
        }
        final HashSet<Domino> leadSet = new HashSet<>(lead);
        final int size = leadSet.size();
        if (size == 1) {
            final SingleSuitMove leadMove = new SingleSuitMove(lead);
            final List<SingleSuitMove> allSingleSuitMoves = findAllSimpleMoves(dominosToBeat);
            for (SingleSuitMove singleSuitMove : allSingleSuitMoves) {
                if (singleSuitMove.beats(leadMove)) {
                    final List<Domino> myMove = times(lead.size(), singleSuitMove.getDomino());
                    removeAll(myMove);
                    return myMove;
                }
            }
        } else {
            final MultiSuitMove leadMultiSuitMove = new MultiSuitMove(lead);
            final List<List<Domino>> allComboMoves = findAllComboMoves(MoveType.BEAT);
            for (List<Domino> allComboMove : allComboMoves) {
                final MultiSuitMove myMultiSuitMove = new MultiSuitMove(allComboMove);
                if (myMultiSuitMove.beats(leadMultiSuitMove)) {
                    final List<Domino> myMove = myMultiSuitMove.extractBeat(leadMultiSuitMove);
                    removeAll(myMove);
                    return myMove;
                }
            }
        }
        return Collections.emptyList();
    }


    private List<Domino> fetch(List<Domino> list, Set<Domino> wishList) {
        final ArrayList<Domino> result = new ArrayList<>();
        for (Domino domino : list) {
            if (wishList.contains(domino)) {
                result.add(domino);
            }
        }
        return result;
    }

    private boolean hasBothSupremes(Collection<Domino> red) {
        final Domino mother = new Domino(2, 4);
        final Domino son = new Domino(1, 2);
        boolean hasMother = false;
        boolean hasSon = false;
        for (Domino domino : red) {
            if (mother.equals(domino)) {
                hasMother = true;
            } else if (son.equals(domino)) {
                hasSon = true;
            }
        }
        return hasMother && hasSon;
    }

    @Override
    public String toString() {
        return "ShiWuHuHand["+name+"]{" +
                "hu=" + hu +
                ", dominos=" + dominosToLead +
                '}';
    }

    @Override
    public void trick(List<Domino> lead) {
        hu.addAll(lead);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public int getHu() {
        return hu.size();

    }

    @Override
    public int getLeftovers() {
        return dominosToLead.size();
    }
}
