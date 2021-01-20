package io.github.navpil.gupai.mod10.taungau;

import io.github.navpil.gupai.Domino;
import io.github.navpil.gupai.mod10.RunGamblingGame;

import java.util.Collection;

public interface Player extends RunGamblingGame.Gambler {

    void deal(Collection<Domino> dominos);

    int placeBet();

    Collection<Domino> discard();

    Collection<Domino> hand();

    void lose(int stake);

    void win(int stake);

    @Override
    int getMoney();
}
