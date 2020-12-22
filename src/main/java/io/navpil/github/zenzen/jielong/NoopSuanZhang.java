package io.navpil.github.zenzen.jielong;

public class NoopSuanZhang implements SuanZhang {
    @Override
    public boolean willSuanZhang(Move move) {
        return false;
    }

    @Override
    public Type executeMove(Move move) {
        return Type.NONE;
    }

    @Override
    public Type suanZhangType() {
        return Type.NONE;
    }
}
