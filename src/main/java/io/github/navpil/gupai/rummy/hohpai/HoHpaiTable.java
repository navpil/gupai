package io.github.navpil.gupai.rummy.hohpai;

import io.github.navpil.gupai.dominos.Domino;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class HoHpaiTable implements Table {

    private final Queue<Domino> dominos = new LinkedList<>();

    private Domino eye;

    private final RuleSet ruleSet;

    public HoHpaiTable(List<Domino> subList, RuleSet ruleSet) {
        if (ruleSet.getFix() == RuleSet.BrokenGameFix.KEEP_AN_EYE) {
            dominos.addAll(subList.subList(1, subList.size()));
            eye = subList.get(0);
            System.out.println("Eye: " + eye);
        } else {
            dominos.addAll(subList);
        }
        this.ruleSet = ruleSet;
    }

    @Override
    public RuleSet getRuleSet() {
        return ruleSet;
    }

    @Override
    public Collection<Domino> getAllDeadDominoes() {
        return Collections.singletonList(eye);
    }

    public Domino remove() {
        return dominos.remove();
    }

    public void discard(Domino discard) {
        dominos.add(discard);
        System.out.println(dominos);
    }

}
