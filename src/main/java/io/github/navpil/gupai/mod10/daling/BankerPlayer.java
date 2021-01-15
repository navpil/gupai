package io.github.navpil.gupai.mod10.daling;

public class BankerPlayer extends RandomPlayer {

    public BankerPlayer(String name) {
        super(name, 0);
    }

    @Override
    public boolean stillHasMoney() {
        return true;
    }

    @Override
    public boolean isBankrupt() {
        return false;
    }
}
