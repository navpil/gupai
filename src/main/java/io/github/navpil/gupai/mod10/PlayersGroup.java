package io.github.navpil.gupai.mod10;

import io.github.navpil.gupai.fishing.tsungshap.NamedPlayer;

import java.util.ArrayList;
import java.util.List;

public class PlayersGroup<T extends NamedPlayer> {

    private final List<T> players;
    private final T banker;

    public PlayersGroup(List<T> allPlayers, int bankerIndex) {
        players = new ArrayList<>();
        for (int i = 0; i < allPlayers.size(); i++) {
            if (i == bankerIndex) continue;
            players.add(allPlayers.get(i));
        }
        if (bankerIndex >= 0 && bankerIndex < allPlayers.size()) {
            banker = allPlayers.get(bankerIndex);
        } else {
            banker = null;
        }
    }

    public List<T> getPlayers() {
        return players;
    }

    public T getBanker() {
        return banker;
    }

}
