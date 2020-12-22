package io.navpil.github.zenzen.jielong;

public interface SuanZhang {

    boolean willSuanZhang(Move move);

    Type executeMove(Move move);

    Type suanZhangType();

    enum Type {
        NONE, SMOTHERED, CLASSIC
    }
}
