package io.navpil.github.zenzen.shiwuhu;

import io.navpil.github.zenzen.dominos.Domino;

import java.util.List;

public class SimpleMove {

    private final Domino domino;
    private final int size;
    private final Suit suit;

    public SimpleMove(List<Domino> dominos) {
        if (dominos.isEmpty()) {
            domino = null;
            size = 0;
            suit = null;
        } else {
            domino = dominos.get(0);
            size = dominos.size();
            suit = Suit.findType(domino);
        }

    }

    public Domino getDomino() {
        return domino;
    }

    public int getSize() {
        return size;
    }

    public Suit getSuit() {
        return suit;
    }

    public boolean beats(SimpleMove leadMove) {
        if (domino == null) {
            return false;
        }
        if (leadMove.domino == null) {
            return true;
        }
        return this.suit == leadMove.suit
                && this.size >= leadMove.size
                && new ShiWuHuComparator().compare(this.domino, leadMove.domino) < 0;
    }
}
