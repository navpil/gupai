package io.navpil.github.zenzen.jielong.player;

import io.navpil.github.zenzen.jielong.Dragon;
import io.navpil.github.zenzen.jielong.Move;

public interface Player {

    Move firstMove();

    Move extractMove(Dragon dragon);

    int getPoints();

    String getName();

}
