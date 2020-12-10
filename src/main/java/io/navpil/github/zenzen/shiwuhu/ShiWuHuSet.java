package io.navpil.github.zenzen.shiwuhu;

import io.navpil.github.zenzen.dominos.Domino;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ShiWuHuSet {

    public static final Set<Domino> WEN = new HashSet<>();
    public static final Set<Domino> RED = new HashSet<>();
    public static final Set<Domino> BLACK = new HashSet<>();

    static {

        //Wen
        for (int i = 1; i <= 6; i++) {
            WEN.add(new Domino(i, i));
        }
        WEN.add(new Domino(6, 5));
        WEN.add(new Domino(6, 4));
        WEN.add(new Domino(1, 6));
        WEN.add(new Domino(1, 5));
        WEN.add(new Domino(1, 3));

        //Red
        RED.add(new Domino(5,4));
        RED.add(new Domino(6,2));
        RED.add(new Domino(4,3));
        RED.add(new Domino(4,2));
        RED.add(new Domino(4,1));
        RED.add(new Domino(2,1));

        //Black
        BLACK.add(new Domino(6,3));
        BLACK.add(new Domino(5,3));
        BLACK.add(new Domino(5,2));
        BLACK.add(new Domino(3,2));

    }


    public static List<Domino> create() {
        final ArrayList<Domino> dominos = new ArrayList<>();
        for (int i = 1; i <= 6; i++) {
            for (int j = i; j <= 6; j++) {
                final Domino domino = new Domino(i, j);
                for (int k = 0; k < 4; k++) {
                    dominos.add(domino);
                }
            }
        }
        return dominos;
    }

}
