package io.github.navpil.gupai;

import io.github.navpil.gupai.dominos.Domino;
import io.github.navpil.gupai.dominos.IDomino;
import io.github.navpil.gupai.dominos.NoDomino;

import java.util.ArrayList;
import java.util.List;

/**
 * Parses dominos from a string
 */
public class DominoParser {

    /**
     * Parses [4:3] format
     * @param substring
     * @return
     */
    public static IDomino parse(String substring) {
        if (substring.charAt(2) == '-') {
            return NoDomino.INSTANCE;
        } else {
            return new Domino(Integer.parseInt(substring.substring(1, 2)), Integer.parseInt(substring.substring(3, 4)));
        }
    }

    /**
     * Parses [4:3], [3:4] format
     */

    public static List<Domino> parseList(String list) {
//        String p = "[3:1], [1:3], [3:3], [3:6], [6:6], [6:5], [5:5], [5:6], [6:6], [6:1], [1:6]";
        final String[] m = list.split(", ");
        List<Domino> dominos = new ArrayList<>();
        for (String move : m) {
            dominos.add(new Domino(Integer.parseInt("" + move.charAt(1)), Integer.parseInt("" + move.charAt(3))));
        }
        return dominos;
    }

    public static List<Domino> parseBag(String bag) {

//        String p = "[1:1]=c:2, [2:2]=c:3, [3:1]=c:3, [3:3]=c:1, [4:3]=c:1, [4:4]=c:3, [5:1]=c:1, [5:2]=c:1, [5:3]=c:1, [6:1]=c:2"
        final String[] m = bag.split(", ");
        List<Domino> dominos = new ArrayList<>();
        for (String move : m) {
            final int count = Integer.parseInt("" + move.charAt(8));
            final Domino d = new Domino(Integer.parseInt("" + move.charAt(1)), Integer.parseInt("" + move.charAt(3)));
            for (int i = 0; i < count; i++) {
                dominos.add(d);
            }
        }
        return dominos;
    }
}
