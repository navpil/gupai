package io.github.navpil.gupai.rummy.smallmahjong;

import io.github.navpil.gupai.fishing.tsungshap.NamedPlayer;
import io.github.navpil.gupai.dominos.Domino;

import java.util.List;

public interface Player extends NamedPlayer {

    void deal(List<Domino> deal);

    void showTable(Table table);

    Triplet offer(Domino lastDiscard, TripletType straights);

    String getName();

    void give(Domino wished);

    boolean hasWon();

    Hand getWinningHand();

    Domino getDiscard();
}
