package io.github.navpil.gupai.shiwuhu.tianjiu;

import io.github.navpil.gupai.dominos.Domino;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Random;

public class RandomComputerPlayer extends AbstractPlayer {

    private final Random random;

    public RandomComputerPlayer(String name) {
        super(name);
        random = new Random();
    }

    @Override
    public Collection<Domino> lead() {
        if (dominos.isEmpty()) {
            return Collections.emptyList();
        }
        final List<Collection<Domino>> lists = new HandHelper(table.getRuleSet()).calculateCombinations(dominos);
        final Collection<Domino> combination = lists.remove(random.nextInt(lists.size()));
        for (Domino domino : combination) {
            dominos.remove(domino);
        }
        return combination;
    }

    @Override
    public Collection<Domino> beat(Collection<Domino> lead) {
        final HandHelper handHelper = new HandHelper(table.getRuleSet());
        final List<Collection<Domino>> lists = handHelper.calculateCombinations(dominos);
        final Optional<Collection<Domino>> any = lists.stream().map(b -> extract(b,lead)).filter(c -> handHelper.canBeat(lead, c)).findAny();
        final Collection<Domino> beat = any.orElse(Collections.emptyList());
        for (Domino domino : beat) {
            dominos.remove(domino);
        }
        return beat;
    }

    private Collection<Domino> extract(Collection<Domino> beat, Collection<Domino> lead) {
        if (beat.size() <= lead.size()) {
            return beat;
        }
        long civils = lead.stream().filter(Domino::isCivil).count();
        long military = lead.size() - civils;

        final ArrayList<Domino> actualBeat = new ArrayList<>();
        for (Domino domino : beat) {
            if (domino.isCivil() && civils > 0) {
                actualBeat.add(domino);
                civils--;
            } else if (military > 0) {
                actualBeat.add(domino);
                military--;
            }
        }
        return actualBeat;
    }

    @Override
    public List<Domino> discard(Collection<Domino> trick) {
        final int size = trick.size();
        final ArrayList<Domino> discard = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            discard.add(dominos.remove(random.nextInt(dominos.size())));
        }
        return discard;
    }
}
