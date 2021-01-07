package io.github.navpil.gupai.dominos;

public class NoDomino implements IDomino {

    public static final NoDomino INSTANCE = new NoDomino();

    public int[] getPips() {
        return new int[0];
    }

    @Override
    public String toString() {
        return "[ - ]";
    }

    @Override
    public int hashCode() {
        return 0;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof NoDomino;
    }
}
