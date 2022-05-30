package io.github.navpil.gupai.mod10.kolyesi;

import io.github.navpil.gupai.Domino;
import io.github.navpil.gupai.mod10.Mod10Rule;

/**
 * This player always wins against Banker if minStake is 10x larger than maxStake.
 *
 * Strategy is simple: always bet 1$, unless it's [6:5], then bet 10$, always take 1 tiles.
 */
public class SimplestComputerPlayer extends PlayerSkeletal {

    protected SimplestComputerPlayer(String name, int money) {
        super(name, money);
    }

    @Override
    public int stake(Domino domino, int minStake, int maxStake) {
        /*
        These are the expected values for every mod10 value domino has, sorted from low to high

        6 : 4.451612903225806
        8 : 4.451612903225806

        4 : 4.473118279569892

        5 : 4.548387096774194

        3 : 4.612903225806452

        0 : 4.741935483870968
        2 : 4.741935483870968
        7 : 4.741935483870968
        9 : 4.741935483870968

        1 : 5.064516129032258
        */

        final int mod10 = Mod10Rule.KOL_YE_SI.getPoints(domino).getMax();
        if (mod10 == 1) {
            return maxStake;
        } else {
            return minStake;
        }
    }

    @Override
    public int dominoCount() {
        return 1;
    }
}
