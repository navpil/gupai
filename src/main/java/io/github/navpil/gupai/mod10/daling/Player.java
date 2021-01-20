package io.github.navpil.gupai.mod10.daling;

import io.github.navpil.gupai.Domino;
import io.github.navpil.gupai.mod10.RunGamblingGame;

import java.util.List;

public interface Player extends RunGamblingGame.Gambler {

    DaLingBet placeBets();

    void deal(List<Domino> deal);

    DaLingHand getHand(Table table);

    void win(Integer stake);

    default void lose(Integer stake) {
        win(-stake);
    }
}
