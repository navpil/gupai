package io.github.navpil.gupai.rummy.smallmahjong;

public enum TripletType {
    /**
     * Two sets, for example [3:3][3:1][1:1] 3x3 + 3x1
     */
    SETS,
    /**
     * One set and one straight, for example [3:3][3:4][5:6] 3x3 + 4-5-6
     */
    MIXED,
    /**
     * Two straights, for example [1:2][3:3][5:4] 1-2-3 + 3-4-5
     */
    STRAIGHTS
}
