package io.navpil.github.zenzen.shiwuhu;

import io.navpil.github.zenzen.dominos.Domino;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;

public class ShiWuHuComparator implements Comparator<Domino> {

    private final HashMap<Domino, Integer> map;

    public ShiWuHuComparator() {
        final ArrayList<Domino> dominos = new ArrayList<>();
        dominos.add(d(6,6));
        dominos.add(d(6,3));
        dominos.add(d(5,4));

        dominos.add(d(1,1));
        dominos.add(d(6,2));
        dominos.add(d(5,3));

        dominos.add(d(4,4));
        dominos.add(d(5,2));
        dominos.add(d(4,3));

        dominos.add(d(4,2));

        dominos.add(d(1,3));
        dominos.add(d(4,1));
        dominos.add(d(3,2));

        dominos.add(d(5,5));
        dominos.add(d(3,3));
        dominos.add(d(2,2));

        dominos.add(d(6,5));
        dominos.add(d(6,4));
        dominos.add(d(1,6));
        dominos.add(d(1,5));

        dominos.add(d(2,1));

        map = new HashMap<>();
        for (int i = 0; i < dominos.size(); i++) {
            map.put(dominos.get(i), i);
        }
    }

    private Domino d(int i, int i1) {
        return new Domino(i, i1);
    }


    @Override
    public int compare(Domino o1, Domino o2) {
        return map.get(o1).compareTo(map.get(o2));
    }
}
