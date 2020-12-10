package io.navpil.github.zenzen.shiwuhu;

import io.navpil.github.zenzen.dominos.Domino;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

//High lead, high beat
public class ShiWuHuHand implements Hand {

    public static final Domino HE = new Domino(1, 3);
    public static final Domino REN = new Domino(4, 4);
    public static final Domino DI = new Domino(1, 1);
    public static final Domino TIAN = new Domino(6, 6);
    private final ArrayList<Domino> hu;
    private final ArrayList<Domino> supremes;
    private final List<Domino> dominos;
    private final String name;

    public ShiWuHuHand(String name, List<Domino> dominos2) {
        this.name = name;
        this.dominos = new ArrayList<>(dominos2);
        this.dominos.sort(new ShiWuHuComparator());


        hu = new ArrayList<>();
        supremes = new ArrayList<>();

        if (hasSupremes(dominos)) {
            final Iterator<Domino> iterator = dominos.iterator();
            final Domino mother = new Domino(2, 4);
            final Domino son = new Domino(1, 2);

            while(iterator.hasNext()) {
                final Domino next = iterator.next();
                if (next.equals(mother) || next.equals(son)) {
                    iterator.remove();
                    hu.add(next);
                    supremes.add(next);
                }
            }
        }

    }

    public List<Domino> lead() {
        if (dominos.isEmpty()) {
            return Collections.emptyList();
        }
        List<List<Domino>> combos = findAllComboMoves();

        combos.sort((l1, l2) -> l2.size() - l1.size());

        final List<Domino> combomove = combos.isEmpty() ? Collections.emptyList() : combos.get(0);
        final List<Domino> simpleMove = findLargestMove();

        if (simpleMove.size() > combomove.size()) {
            dominos.removeAll(simpleMove);
            return simpleMove;
        } else {
            dominos.removeAll(combomove);
            return combomove;
        }
    }

    private List<List<Domino>> findAllComboMoves() {
        List<Domino> tianJiu = fetch(dominos, Set.of(TIAN, new Domino(6,3), new Domino(5,4)));
        List<Domino> diBa = fetch(dominos, Set.of(DI, new Domino(5,3), new Domino(2,6)));
        List<Domino> renQi = fetch(dominos, Set.of(REN, new Domino(5,2), new Domino(3,4)));
        List<Domino> heWu = fetch(dominos, Set.of(HE, new Domino(3,2), new Domino(4,1)));

        List<List<Domino>> combos = new ArrayList<>();
        combos.add(getLargeComboMove(tianJiu, TIAN));
        combos.add(getLargeComboMove(diBa, DI));
        combos.add(getLargeComboMove(renQi, REN));
        combos.add(getLargeComboMove(heWu, HE));
        return combos;
    }

    private List<Domino> findLargestMove() {
        Domino d = dominos.get(0);
        int counter = 1;
        int largestCounter = 1;
        Domino largestDomino = d;
        for (int i = 1; i < dominos.size(); i++) {
            final Domino next = dominos.get(i);
            if (d.equals(next)) {
                counter++;
                if (counter == 4) {
                    return times(4, d);
                }
            } else {
                if (counter > largestCounter) {
                    largestCounter = counter;
                    largestDomino = d;
                }
                d = next;
                counter = 1;
            }
        }
        if (counter > largestCounter) {
            largestCounter = counter;
            largestDomino = d;
        }
        return times(largestCounter, largestDomino);
    }

    private List<SimpleMove> findAllSimpleMoves() {
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
        if (dominos.isEmpty()) {
            return Collections.emptyList();
        }
        final HashSet<Domino> leadSet = new HashSet<>(lead);
        final int size = leadSet.size();
        if (size == 1) {
            final SimpleMove leadMove = new SimpleMove(lead);
            final List<SimpleMove> allSimpleMoves = findAllSimpleMoves();
            for (SimpleMove simpleMove : allSimpleMoves) {
                if (simpleMove.beats(leadMove)) {
                    final List<Domino> myMove = times(lead.size(), simpleMove.getDomino());
                    dominos.removeAll(myMove);
                    return myMove;
                }
            }
        } else {
            final ComboMove leadComboMove = new ComboMove(lead);
            final List<List<Domino>> allComboMoves = findAllComboMoves();
            for (List<Domino> allComboMove : allComboMoves) {
                final ComboMove myComboMove = new ComboMove(allComboMove);
                if (myComboMove.beats(leadComboMove)) {
                    final List<Domino> myMove = myComboMove.extractBeat(leadComboMove);
                    dominos.removeAll(myMove);
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
                ", dominos=" + dominos +
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
        return dominos.size();
    }
}
