package io.github.navpil.gupai.mod10.taungau;

public class BankerPlayer extends ComputerPlayer {

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
