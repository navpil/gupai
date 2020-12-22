package io.navpil.github.zenzen.jielong;

import io.navpil.github.zenzen.dominos.Domino;

public class Move {

    private final int side;
    private final int inwards;
    private final int outwards;
    private boolean suanZhang;

    private Domino cacheDomino;

    public Move(int side, int inwards, int outwards) {
        this.side = side;
        this.inwards = inwards;
        this.outwards = outwards;
    }

    public boolean isSuanZhang() {
        return suanZhang;
    }

    public void setSuanZhang(boolean suanZhang) {
        this.suanZhang = suanZhang;
    }

    public Domino getDomino() {
        if (cacheDomino == null) {
            cacheDomino = new Domino(inwards, outwards);
        }
        return cacheDomino;
    }

    public static Move none(Domino dominoToPut) {
        return new Move(-1, dominoToPut.getPips()[0], dominoToPut.getPips()[1]);
    }

    public static Move lead(Domino dominoToPut) {
        return new Move(0, dominoToPut.getPips()[0], dominoToPut.getPips()[1]);
    }

    public int getSide() {
        return side;
    }

    public int getInwards() {
        return inwards;
    }

    public int getOutwards() {
        return outwards;
    }

    @Override
    public String toString() {
        return "Move{" +
                "side=" + side +
                ", inwards=" + inwards +
                ", outwards=" + outwards +
                '}';
    }
}
