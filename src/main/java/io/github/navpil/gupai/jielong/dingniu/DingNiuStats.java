package io.github.navpil.gupai.jielong.dingniu;

import io.github.navpil.gupai.util.Stats;

public class DingNiuStats extends Stats {

    private int suanZhangPlayer = -1;
    private boolean gameBlocked;

    public void setSuanZhangPlayer(int suanZhangPlayer) {
        this.suanZhangPlayer = suanZhangPlayer;
    }

    public int getSuanZhangPlayer() {
        return suanZhangPlayer;
    }

    public void setGameBlocked(boolean gameBlocked) {
        this.gameBlocked = gameBlocked;
    }

    public boolean getGameBlocked() {
        return gameBlocked;
    }
}
