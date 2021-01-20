package io.github.navpil.gupai;

public enum CombinationType {

    //ssang-syo - all different
    DRAGON,

    //tai-sam-tong
    SPLIT,

    //sok (coincidence and five sons)
    FIVE_SONS,
    COINCIDENCE,

    //ssang-pyen (Doublets?)
    ER_SAN_KAO,
    //a-ki Child
    SMALL_THREE,
    //ro-in Old Man
    BIG_THREE,


    //MIDDLE_DRAGON -> used instead of er-san-kao
    MIDDLE_DRAGON,

    //Used in XiangShiFu - not used for HoHpai
    FIVE_POINTS,
    SPLENDID,

    //Ho-hpai specials
    STRAIGHT,
    THREE_PAIRS,

    //Civil Pairs
    HEAVEN(true),
    EARTH(true),
    MAN(true),
    HARMONY(true),
    PLUM(true),
    LONG_THREE(true),
    BENCH(true),
    AXE(true),
    RED_HEAD(true),
    LONG_LEGS(true),
    BIG_HEAD(true),

    //Chinese military pairs
    NINES(true),
    EIGHTS(true),
    SEVENS(true),
    FIVES(true),

    SUPREME_PAIR(true),

    MILITARY_KOREAN_PAIR(true),

    none;

    private final boolean pair;

    CombinationType(boolean isPair) {
        this.pair = isPair;
    }

    CombinationType() {
        this(false);
    }

    public boolean isPair() {
        return pair;
    }
}
