package io.navpil.github.zenzen.rummy.kapshap;

import io.navpil.github.zenzen.dominos.Domino;

import java.util.List;

public interface KapShapPlayer {

    void showTable(KapShapTableVisibleInformation table);

    void deal(List<Domino> dominoes);

    Domino offer(List<Domino> offer);

    void give(Domino domino);

    Domino getDiscard();

    boolean hasWon();

    KapShapHand getWinningHand();

    String getName();

}
