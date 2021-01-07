package io.github.navpil.gupai.shiwuhu;

import io.github.navpil.gupai.dominos.Domino;

import java.util.HashSet;
import java.util.Set;

public enum Suit {
    WEN, WU_RED, WU_BLACK;

    private static final Set<Domino> WEN_DOMINOES = new HashSet<>();
    private static final Set<Domino> RED = new HashSet<>();
    private static final Set<Domino> BLACK = new HashSet<>();

    static {

        //Wen
        for (int i = 1; i <= 6; i++) {
            WEN_DOMINOES.add(new Domino(i, i));
        }
        WEN_DOMINOES.add(new Domino(6, 5));
        WEN_DOMINOES.add(new Domino(6, 4));
        WEN_DOMINOES.add(new Domino(1, 6));
        WEN_DOMINOES.add(new Domino(1, 5));
        WEN_DOMINOES.add(new Domino(1, 3));

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


    public static Suit findType(Domino domino) {
        if (RED.contains(domino)) {
            return Suit.WU_RED;
        }
        if (BLACK.contains(domino)) {
            return Suit.WU_BLACK;
        }
        return Suit.WEN;
    }


}
