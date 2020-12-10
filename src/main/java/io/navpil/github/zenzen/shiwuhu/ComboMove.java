package io.navpil.github.zenzen.shiwuhu;

import io.navpil.github.zenzen.dominos.Domino;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ComboMove {

    private final HashMap<Suit, SimpleMove> map2;

    public ComboMove(List<Domino> dominos) {
        final HashMap<Suit, List<Domino>> map = new HashMap<>();
        map2 = new HashMap<>();
        for (Suit value : Suit.values()) {
            map.put(value, new ArrayList<>());
        }
        for (Domino domino : dominos) {
            map.get(Suit.findType(domino)).add(domino);
        }

        for (Suit suit : map.keySet()) {
            map2.put(suit, new SimpleMove(map.get(suit)));
        }
    }

    public boolean beats(ComboMove comboMove) {
        boolean beats = true;
        for (Suit suit : Suit.values()) {
            beats = beats && map2.get(suit).beats(comboMove.map2.get(suit));
        }
        return beats;
    }

    public List<Domino> extractBeat(ComboMove comboMove) {
        final ArrayList<Domino> result = new ArrayList<>();

        for (Suit suit : Suit.values()) {
            final SimpleMove simpleLead = comboMove.map2.get(suit);
            result.addAll(times(simpleLead.getSize(), map2.get(suit).getDomino()));

        }
        return result;


    }

    private List<Domino> times(int largestCounter, Domino largestDomino) {
        final ArrayList<Domino> result = new ArrayList<>();
        for (int i = 0; i < largestCounter; i++) {
            result.add(largestDomino);
        }
        return result;
    }


}
