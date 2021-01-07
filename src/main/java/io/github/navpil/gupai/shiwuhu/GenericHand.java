package io.github.navpil.gupai.shiwuhu;

import io.github.navpil.gupai.dominos.Domino;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

//High lead, high beat
public class GenericHand implements Hand {

    public static final Domino HE = new Domino(1, 3);
    public static final Domino REN = new Domino(4, 4);
    public static final Domino DI = new Domino(1, 1);
    public static final Domino TIAN = new Domino(6, 6);
    private final ArrayList<Domino> hu;
    private final ArrayList<Domino> supremes;
    private final List<Domino> dominosToLead;
    private final List<Domino> dominosToBeat;
    private final String name;
    private final Strategy strategy;

    private enum MoveType {
        LEAD, BEAT;
    }

    public static class Strategy {

        private final boolean leadHighFirst;

        private final boolean leadCombinationFirstIfTie;
        private final boolean alwaysLeadCombinationFirst;

        private final boolean leadBroadFirst;
        private final boolean broadOverHigh;

        private final boolean beatHigh;

        public Strategy(boolean leadBroadFirst, boolean leadHighFirst, boolean leadCombinationFirstIfTie, boolean alwaysLeadCombinationFirst, boolean beatHigh, boolean broadOverHigh) {
            this.leadHighFirst = leadHighFirst;
            this.leadCombinationFirstIfTie = leadCombinationFirstIfTie;
            this.alwaysLeadCombinationFirst = alwaysLeadCombinationFirst;
            this.beatHigh = beatHigh;

            this.leadBroadFirst = leadBroadFirst;
            this.broadOverHigh = broadOverHigh;
        }
    }

    public GenericHand(String name, List<Domino> dominos2, Strategy strategy) {
        this.name = name;
        this.strategy = strategy;
        this.dominosToLead = new ArrayList<>(dominos2);
        this.dominosToBeat = new ArrayList<>(dominos2);
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
        supremes = new ArrayList<>();

        if (hasSupremes(dominos2)) {
            final Domino mother = new Domino(2, 4);
            final Domino son = new Domino(1, 2);
            for (Domino domino : dominos2) {
                if (domino.equals(mother) || domino.equals(son)) {
                    hu.add(domino);
                    supremes.add(domino);
                }
            }
            removeAll(hu);
        }

    }

    private void removeAll(Collection<Domino> dominos) {
        dominosToBeat.removeAll(dominos);
        dominosToLead.removeAll(dominos);
    }

    public List<Domino> lead() {
        if (dominosToLead.isEmpty()) {
            return Collections.emptyList();
        }
        List<List<Domino>> combos = findAllComboMoves(MoveType.LEAD);

        if (strategy.broadOverHigh) {
            if (strategy.leadBroadFirst) {
                combos.sort((l1, l2) -> l2.size() - l1.size());
            } else {
                combos.sort((l1, l2) -> l1.size() - l2.size());
            }
        }

        final List<Domino> combomove = combos.isEmpty() ? Collections.emptyList() : combos.get(0);
        if (strategy.alwaysLeadCombinationFirst && !combomove.isEmpty()) {
            removeAll(combomove);
            return combomove;
        }

        final List<SimpleMove> simpleMoves = findAllSimpleMoves(dominosToLead);

        if (strategy.broadOverHigh) {
            if (strategy.leadBroadFirst) {
                simpleMoves.sort((l1, l2) -> l2.getSize() - l1.getSize());
            } else {
                simpleMoves.sort((l1, l2) -> l1.getSize() - l2.getSize());
            }
        }

        final SimpleMove simpleMove2 = simpleMoves.get(0);
        List<Domino> simpleMove = times(simpleMove2.getSize(), simpleMove2.getDomino());

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
//
//    private List<RealDomino> findLargestMove(List<RealDomino> dominos) {
//        RealDomino d = dominos.get(0);
//        int counter = 1;
//        int largestCounter = 1;
//        RealDomino largestDomino = d;
//        for (int i = 1; i < dominos.size(); i++) {
//            final RealDomino next = dominos.get(i);
//            if (d.equals(next)) {
//                counter++;
//                if (counter == 4) {
//                    return times(4, d);
//                }
//            } else {
//                if (counter > largestCounter) {
//                    largestCounter = counter;
//                    largestDomino = d;
//                }
//                d = next;
//                counter = 1;
//            }
//        }
//        return times(largestCounter, largestDomino);
//    }

    private List<SimpleMove> findAllSimpleMoves(List<Domino> dominos) {
        final ArrayList<SimpleMove> result = new ArrayList<>();
        Domino d = dominos.get(0);
        int counter = 1;
        for (int i = 1; i < dominos.size(); i++) {
            final Domino next = dominos.get(i);
            if (d.equals(next)) {
                counter++;
            } else {
                result.add(new SimpleMove(times(counter, d)));
                d = next;
                counter = 1;
            }
        }
        result.add(new SimpleMove(times(counter, d)));
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

    public List<Domino> beat(List<Domino> lead) {
        if (dominosToBeat.isEmpty()) {
            return Collections.emptyList();
        }
        final HashSet<Domino> leadSet = new HashSet<>(lead);
        final int size = leadSet.size();
        if (size == 1) {
            final SimpleMove leadMove = new SimpleMove(lead);
            final List<SimpleMove> allSimpleMoves = findAllSimpleMoves(dominosToBeat);
            for (SimpleMove simpleMove : allSimpleMoves) {
                if (simpleMove.beats(leadMove)) {
                    final List<Domino> myMove = times(lead.size(), simpleMove.getDomino());
                    removeAll(myMove);
                    return myMove;
                }
            }
        } else {
            final ComboMove leadComboMove = new ComboMove(lead);
            final List<List<Domino>> allComboMoves = findAllComboMoves(MoveType.BEAT);
            for (List<Domino> allComboMove : allComboMoves) {
                final ComboMove myComboMove = new ComboMove(allComboMove);
                if (myComboMove.beats(leadComboMove)) {
                    final List<Domino> myMove = myComboMove.extractBeat(leadComboMove);
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

    private boolean hasSupremes(List<Domino> red) {
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
                "supremes=" + supremes +
                "hu=" + hu +
                ", dominos=" + dominosToLead +
                '}';
    }

    public void trick(List<Domino> lead) {
        hu.addAll(lead);
    }

    public String getName() {
        return name;
    }

    public int getHu() {
        return hu.size();

    }

    @Override
    public int getLeftovers() {
        return dominosToLead.size();
    }
}
