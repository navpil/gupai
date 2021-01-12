package io.github.navpil.gupai.shiwuhu;

import io.github.navpil.gupai.dominos.Domino;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class ShiWuHuHandHelper {

    public static final Domino HE = new Domino(1, 3);
    public static final Domino REN = new Domino(4, 4);
    public static final Domino DI = new Domino(1, 1);
    public static final Domino TIAN = new Domino(6, 6);

    public static final Set<SpecialWithAGlueCard> specials;
    public static final Set<Domino> tianJiu = Set.of(TIAN, new Domino(6,3), new Domino(5,4));
    public static final Set<Domino> diBa = Set.of(DI, new Domino(5,3), new Domino(2,6));
    public static final Set<Domino> renQi = Set.of(REN, new Domino(5,2), new Domino(3,4));
    public static final Set<Domino> heWu = Set.of(HE, new Domino(3,2), new Domino(4,1));
    static {
        specials = Set.of(
                new SpecialWithAGlueCard(TIAN, tianJiu),
                new SpecialWithAGlueCard(DI, diBa),
                new SpecialWithAGlueCard(REN, renQi),
                new SpecialWithAGlueCard(HE, heWu)
        );
    }

    public static boolean isValidHand(Collection<Domino> dominos) {
        if (dominos.size() == 1) {
            return true;
        }
        final HashSet<Domino> set = new HashSet<>(dominos);
        if (set.size() == 1) {
            return true;
        }
        for (SpecialWithAGlueCard special : specials) {
            if (special.isValid(set)) {
                return true;
            }
        }
        return false;

    }

    private static class SpecialWithAGlueCard {
        private final Domino glueCard;
        private final Set<Domino> set;

        private SpecialWithAGlueCard(Domino glueCard, Set<Domino> set) {
            this.glueCard = glueCard;
            this.set = set;
        }

        public boolean isValid(HashSet<Domino> set) {
            return this.set.containsAll(set) && set.contains(glueCard);
        }
    }

}
