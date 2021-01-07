package io.github.navpil.gupai.rummy.kapshap;

import io.github.navpil.gupai.dominos.Domino;

import java.util.ArrayList;
import java.util.List;

public class KapShapTableVisibleInformation {

    private final int[] mods = new int[10];
    private final List<Domino> openDominoes = new ArrayList<>();
    private final KapShapRuleset rules;
    private Domino lastDiscarded;

    public KapShapTableVisibleInformation(List<Domino> set, KapShapRuleset rules) {
        for (Domino domino : set) {
            mods[Mod10Calculation.mod10(domino)]++;
        }
        this.rules = rules;
    }
    public KapShapTableVisibleInformation(List<Domino> set) {
        this(set, new KapShapRuleset(KapShapRuleset.Offer.ALL, true, 1, 8, false));
    }

    private KapShapTableVisibleInformation(int[] mods, List<Domino> openDominoes, KapShapRuleset rules) {
        System.arraycopy(mods, 0, this.mods, 0, 10);
        this.openDominoes.addAll(openDominoes);
        this.rules = rules;
    }

    public static KapShapTableVisibleInformation copyOf(KapShapTableVisibleInformation table) {
        return new KapShapTableVisibleInformation(table.mods, table.openDominoes, table.rules);
    }

    public int[] getTotalFrequencies() {
        final int[] ints = new int[10];
        System.arraycopy(mods, 0, ints, 0, 10);
        return ints;
    }

    public List<Domino> getOpenDominoes() {
        return openDominoes;
    }

    public void add(Domino domino) {
        openDominoes.add(domino);
        lastDiscarded = domino;
    }

    public void remove(Domino domino) {
        openDominoes.remove(domino);
    }

    public Domino getLastDiscarded() {
        return lastDiscarded;
    }

    public KapShapRuleset getRules() {
        return rules;
    }
}
