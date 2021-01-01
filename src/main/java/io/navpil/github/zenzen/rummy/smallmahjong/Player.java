package io.navpil.github.zenzen.rummy.smallmahjong;

import io.navpil.github.zenzen.dominos.Domino;

import java.util.List;

public interface Player {

    void deal(List<Domino> deal);

    void showTable(Table table);

    Triplet offer(Domino lastDiscard, TripletType straights);

    String getName();

    void give(Domino wished);

    boolean hasWon();

    Hand getWinningHand();

    Domino getDiscard();
}
