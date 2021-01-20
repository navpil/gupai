package io.github.navpil.gupai.jielong.dingniu;

import io.github.navpil.gupai.jielong.Move;

public interface SuanZhang {

    boolean willSuanZhang(Move move);

    Type executeMove(Move move);

    Type suanZhangType();

    enum Type {
        NONE, SMOTHERED, CLASSIC
    }
}
