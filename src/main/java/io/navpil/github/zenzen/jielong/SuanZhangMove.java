package io.navpil.github.zenzen.jielong;

public class SuanZhangMove extends Move{


    private final boolean isSuanZhang;

    public SuanZhangMove(int side, int inwards, int outwards, boolean isSuanZhang) {
        super(side, inwards, outwards);
        this.isSuanZhang = isSuanZhang;
    }
    public SuanZhangMove(Move move, boolean isSuanZhang) {
        this(move.getSide(), move.getInwards(), move.getOutwards(), isSuanZhang);
    }

    public boolean isSuanZhang() {
        return isSuanZhang;
    }
}
