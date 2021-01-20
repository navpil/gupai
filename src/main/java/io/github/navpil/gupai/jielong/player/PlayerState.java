package io.github.navpil.gupai.jielong.player;

import io.github.navpil.gupai.Domino;

import java.util.Collection;
import java.util.List;

public class PlayerState {

    private final List<Domino> dominos;
    private final Collection<Domino> putDown;

    public PlayerState(List<Domino> dominos, Collection<Domino> putDown) {
        this.dominos = dominos;
        this.putDown = putDown;
    }

    public List<Domino> getDominos() {
        return dominos;
    }

    public Collection<Domino> getPutDown() {
        return putDown;
    }
}
