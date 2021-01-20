package io.github.navpil.gupai.jielong.player;

import io.github.navpil.gupai.util.CombineCollection;
import io.github.navpil.gupai.Domino;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public abstract class AbstractPlayer implements Player {

    protected final String name;
    protected List<Domino> dominos;
    protected final Collection<Domino> putDown = new ArrayList<>();

    public AbstractPlayer(String name) {
        this.name = name;
    }

    @Override
    public Collection<Domino> leftOvers() {
        return new CombineCollection<>(List.of(dominos, putDown));
    }

    @Override
    public Player deal(Collection<Domino> dealt) {
        putDown.clear();
        this.dominos = new ArrayList<>(dealt);
        return this;
    }

    @Override
    public int getPoints() {
        int score = 0;
        for (Domino domino : dominos) {
            score += (domino.getPips()[0] + domino.getPips()[1]);
        }
        for (Domino domino : putDown) {
            score += (domino.getPips()[0] + domino.getPips()[1]);
        }
        return score;
    }

    @Override
    public String getName() {
        return name;
    }
}
