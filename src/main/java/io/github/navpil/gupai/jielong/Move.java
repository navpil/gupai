package io.github.navpil.gupai.jielong;

import io.github.navpil.gupai.Domino;

/**
 * JieLong move
 */
public class Move {

    /**
     * Side shows where to put a tile.
     *
     * Numbers mean:
     * <ul>
     *     <li>1 means side 1 (right)</li>
     *     <li>2 means side 2 (left)</li>
     *     <li>0 means a leading tile (outwards/right side being opened)</li>
     *     <li>-1 means discard</li>
     * </ul>
     */
    private final int side;
    /**
     * Pip pointing inwards - to the dragon
     */
    private final int inwards;
    /**
     * Pip pointing outwards - becomes a new opened end
     */
    private final int outwards;
    /**
     * May the move result in a SuanZhang
     */
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
