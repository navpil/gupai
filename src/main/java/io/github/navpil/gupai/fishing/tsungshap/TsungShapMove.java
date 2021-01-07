package io.github.navpil.gupai.fishing.tsungshap;

import io.github.navpil.gupai.dominos.Domino;

import java.util.Objects;

public class TsungShapMove {

    public enum Type {
        DISCARD, PAIR, TRIPLET;
    }

    public enum Side {
        LEFT, RIGHT, BOTH;
    }

    private final Type type;
    private final Side side;
    private final Domino domino;

    public TsungShapMove(Type type, Side side, Domino domino) {
        this.type = type;
        this.side = side;
        this.domino = domino;
    }

    public Type getType() {
        return type;
    }

    public Side getSide() {
        return side;
    }

    public Domino getDomino() {
        return domino;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TsungShapMove that = (TsungShapMove) o;
        return type == that.type &&
                side == that.side &&
                Objects.equals(domino, that.domino);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, side, domino);
    }

    @Override
    public String toString() {
        return "TsungShapMove{" +
                "type=" + type +
                ", side=" + side +
                ", domino=" + domino +
                '}';
    }
}
