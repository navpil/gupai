package io.github.navpil.gupai.tricks.shiwuhu;

import io.github.navpil.gupai.Domino;
import io.github.navpil.gupai.fishing.tsungshap.NamedPlayer;

import java.util.Collection;
import java.util.List;

public interface Player extends NamedPlayer {

    void deal(Collection<Domino> deal);

    /**
     * What a player wants to lead to a trick with
     *
     * @return dominoes to lead toa trick with
     */
    List<Domino> lead();

    String getName();

    /**
     * Player takes a trick (hu increases)
     *
     * @param trick trick which a player won
     */
    void trick(List<Domino> trick);

    /**
     * How the player wants to beat a trick.
     *
     * Empty if a player does not want to beat.
     *
     * @param lead what to beat
     * @return what to beat with
     */
    List<Domino> beat(List<Domino> lead);

    /**
     * How many points a player has
     *
     * @return hu (points)
     */
    int getHu();

    /**
     * How many cards a player still has
     *
     * @return cards player still has
     */
    int getLeftovers();
}
