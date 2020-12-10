package io.navpil.github.zenzen;

import io.navpil.github.zenzen.dominos.IDomino;
import io.navpil.github.zenzen.dominos.NoDomino;
import io.navpil.github.zenzen.dominos.Domino;

public class DominoFactory {

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
}
