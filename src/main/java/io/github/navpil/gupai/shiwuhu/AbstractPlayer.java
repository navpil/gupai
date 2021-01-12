package io.github.navpil.gupai.shiwuhu;

import io.github.navpil.gupai.dominos.Domino;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public abstract class AbstractPlayer implements Player {

    private final String name;
    protected List<Domino> dominos;
    private ArrayList<Domino> hu;

    public AbstractPlayer(String name) {
        this.name = name;
    }

    @Override
    public void deal(Collection<Domino> dominos) {
        this.dominos = new ArrayList<>(dominos);
        this.hu = new ArrayList<>();

        if (hasBothSupremes(dominos)) {
            final List<Domino> supremesSet = Domino.ofList(42, 21);
            hu.addAll(dominos.stream().filter(supremesSet::contains).collect(Collectors.toList()));
            dominoesRemoved(hu);
        }
    }

    protected <Dominoes extends Collection<Domino>> Dominoes dominoesRemoved(Dominoes dominos) {
        for (Domino domino : dominos) {
            this.dominos.remove(domino);
        }
        return dominos;
    }

    private boolean hasBothSupremes(Collection<Domino> red) {
        final Domino mother = new Domino(2, 4);
        final Domino son = new Domino(1, 2);
        boolean hasMother = false;
        boolean hasSon = false;
        for (Domino domino : red) {
            if (mother.equals(domino)) {
                hasMother = true;
            } else if (son.equals(domino)) {
                hasSon = true;
            }
        }
        return hasMother && hasSon;
    }

    @Override
    public String toString() {
        return "ShiWuHuHand["+name+"]{" +
                "hu=" + hu +
                ", dominos=" + dominos +
                '}';
    }

    @Override
    public void trick(List<Domino> lead) {
        hu.addAll(lead);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public int getHu() {
        return hu.size();

    }

    @Override
    public int getLeftovers() {
        return dominos.size();
    }

}
