package io.github.navpil.gupai.rummy.kapshap;

import io.github.navpil.gupai.fishing.tsungshap.NamedPlayer;
import io.github.navpil.gupai.Domino;

import java.util.List;

public interface KapShapPlayer extends NamedPlayer {

    void showTable(KapShapTableVisibleInformation table);

    void deal(List<Domino> dominoes);

    Domino offer(List<Domino> offer);

    void give(Domino domino);

    Domino getDiscard();

    boolean hasWon();

    KapShapHand getWinningHand();

    String getName();

}
