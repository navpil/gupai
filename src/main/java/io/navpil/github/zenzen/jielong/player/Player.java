package io.navpil.github.zenzen.jielong.player;

import io.navpil.github.zenzen.dominos.Domino;
import io.navpil.github.zenzen.jielong.Dragon;
import io.navpil.github.zenzen.jielong.Move;

import java.util.Collection;

/**
 * Player interface for "connecting" games
 */
public interface Player {

    Collection<Domino> leftOvers();

    Player deal(Collection<Domino> dealt);
    /**
     * The move which starts the dragon.
     *
     * Should update the inner player state, but not the state of the dragon
     *
     * @return leading move
     */
    Move firstMove();

    /**
     * The ordinary move - it can be a connecting move or a discard
     *
     * Should update the inner player state, but not the state of the dragon
     *
     * @param dragon dragon to connect to
     * @return ordinary move
     */
    Move extractMove(Dragon dragon);

    /**
     * How many points a player has in his hand
     *
     * @return points
     */
    int getPoints();

    /**
     * Player name - must be unique among players
     *
     * @return player name
     */
    String getName();

}
