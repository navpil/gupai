package io.github.navpil.gupai.tricks.shiwuhu;

import io.github.navpil.gupai.Domino;

import java.util.List;
import java.util.Objects;

/**
 * Describes a move which only consists of a single suit
 */
public class SingleSuitMove {

    private final Domino domino;
    private final int size;
    private final Suit suit;

    public SingleSuitMove(List<Domino> dominos) {
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

    public boolean beats(SingleSuitMove leadMove) {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SingleSuitMove that = (SingleSuitMove) o;
        return size == that.size &&
                Objects.equals(domino, that.domino) &&
                suit == that.suit;
    }

    @Override
    public int hashCode() {
        return Objects.hash(domino, size, suit);
    }

    @Override
    public String toString() {
        return "SingleSuitMove{" +
                "domino=" + domino +
                ", size=" + size +
                ", suit=" + suit +
                '}';
    }
}
