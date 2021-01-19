package io.github.navpil.gupai.mod10.paigow;

import io.github.navpil.gupai.dominos.Domino;
import io.github.navpil.gupai.mod10.RunGamblingGame;

import java.util.List;

public interface Player extends RunGamblingGame.Gambler {

    void deal(List<Domino> take);

    Hand hand();

    void win(int stake);

    default void lose(int stake) {
        win(-stake);
    }

    int stake(int maxStake);
}
