package io.navpil.github.zenzen;

import java.util.Set;

public enum Combination {

    none,

    SUPREME,

    HEAVEN,
    EARTH,
    MAN,
    HARMONY,
    PLUM,
    LONG_THREE,
    BENCH,
    AXE,
    RED_HEAD,
    LONG_LEGS,
    BIG_HEAD,

    NINES,
    EIGHTS,
    SEVENS,
    FIVES,

    GENERIC_CIVIL_PAIR,
    GENERIC_MILITARY_PAIR,

    //Five points: also known as fragrant five, three with five points, three cards have exactly the same three numbers, and the sum of the remaining numbers equals five.
    FIVE_POINTS,

    //Positive fast: also known as full fourteen, three cards have exactly the same three numbers, and the sum of the remaining numbers is equal to fourteen or more.
    SPLENDID,

    //Split phase: also known as split compartment, three cards have exactly the same three numbers; the other three numbers are also the same.
    SPLIT,

    //Coincidentally: three cards have exactly the same four numbers, and the sum of the remaining numbers is equal to the former number.
    COINCIDENCE,

    //Five sons: Three cards have exactly the same five numbers.
    FIVE_SONS,

    //Different: Also known as the whole dragon, the six numbers in the three cards are different.
    DRAGON,
    //Horse Army: Also known as Big Dragon, Double Four, Five and Six, two of the three cards are 4, 5, and 6.
    BIG_THREE,
    //Mo 2 3: Also known as Small Dragon, Pair of Mo 2 3, two of the three cards are 1, 2, and 3.
    SMALL_THREE,
    //Mixed Dragon: Two each of 3 and 4, and one each of 2, 5.
    MIDDLE_FOUR;

    public boolean isPair() {
        return PAIRS.contains(this);
    }

    public boolean isMilitary() {
        return MILITARY.contains(this);
    }

    public boolean isCivilMinister() {
        return CIVIL_MINISTERS.contains(this);
    }

    private static final Set<Combination> PAIRS = Set.of(
            SUPREME,
            HEAVEN,
            EARTH,
            MAN,
            HARMONY,
            PLUM,
            LONG_THREE,
            BENCH,
            AXE,
            RED_HEAD,
            LONG_LEGS,
            BIG_HEAD,
            NINES,
            EIGHTS,
            SEVENS,
            FIVES
    );

    private static final Set<Combination> CIVIL = Set.of(
            HEAVEN,
            EARTH,
            MAN,
            HARMONY,
            PLUM,
            LONG_THREE,
            BENCH,
            AXE,
            RED_HEAD,
            LONG_LEGS,
            BIG_HEAD
    );

    private static final Set<Combination> CIVIL_MINISTERS = Set.of(
            HEAVEN,
            EARTH,
            MAN,
            HARMONY
    );

    private static final Set<Combination> MILITARY = Set.of(
            NINES,
            EIGHTS,
            SEVENS,
            FIVES
    );

}
