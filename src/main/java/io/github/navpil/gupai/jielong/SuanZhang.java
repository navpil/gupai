package io.github.navpil.gupai.jielong;

public interface SuanZhang {

    boolean willSuanZhang(Move move);

    Type executeMove(Move move);

    Type suanZhangType();

    enum Type {
        NONE, SMOTHERED, CLASSIC
    }
}
