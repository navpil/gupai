package io.github.navpil.gupai.mod10.taungau;

import io.github.navpil.gupai.Domino;
import io.github.navpil.gupai.mod10.Mod10Rule;
import io.github.navpil.gupai.util.CollectionUtil;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.stream.Collectors;

public class ComputerPlayer extends AbstractPlayer {

    public ComputerPlayer(String name, int money) {
        super(name, money);
    }

    @Override
    public int placeBet() {
        return Math.min(getMoney(), 3);
    }

    @Override
    public Collection<Domino> discard() {
        final Collection<Collection<Domino>> possibleDiscards = CollectionUtil.allPermutations(dominos, 3);
        final Collection<Collection<Domino>> dominos = possibleDiscards.stream().filter(d -> TauNgau.MOD_10_RULE.getPoints(d).isMod10()).collect(Collectors.toSet());
        final Collection<Domino> discard = calculateBestDiscard(dominos);
        this.dominos.strictRemoveAll(discard);
        return discard;
    }

    private Collection<Domino> calculateBestDiscard(Collection<Collection<Domino>> dominos) {
        if (dominos.isEmpty()) {
            return Collections.emptyList();
        } else if (dominos.size() == 1) {
            return dominos.iterator().next();
        } else {
            final Iterator<Collection<Domino>> discards = dominos.iterator();
            Collection<Domino> discard = discards.next();
            long supremesCount = discard.stream().filter(Mod10Rule.SUPREMES::contains).count();
            if (supremesCount == 0) {
                return discard;
            }
            while (discards.hasNext()) {
                final Collection<Domino> currentDiscard = discards.next();
                final long currentSupremesCount = discard.stream().filter(Mod10Rule.SUPREMES::contains).count();
                if (currentSupremesCount == 0) {
                    return currentDiscard;
                }
                if (currentSupremesCount < supremesCount) {
                    supremesCount = currentSupremesCount;
                    discard = currentDiscard;
                }
            }
            return discard;
        }
    }

}
