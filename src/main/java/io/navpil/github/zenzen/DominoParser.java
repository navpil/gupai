package io.navpil.github.zenzen;

import io.navpil.github.zenzen.dominos.IDomino;
import io.navpil.github.zenzen.dominos.NoDomino;
import io.navpil.github.zenzen.dominos.Domino;
import io.navpil.github.zenzen.jielong.Move;

import java.util.ArrayList;
import java.util.List;

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
}
