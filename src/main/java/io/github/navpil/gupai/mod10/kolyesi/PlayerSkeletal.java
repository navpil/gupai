package io.github.navpil.gupai.mod10.kolyesi;

abstract class PlayerSkeletal implements KolYeSiPlayer {

    private final String name;
    private int money;

    protected PlayerSkeletal(String name, int money) {
        this.name = name;
        this.money = money;
    }

    @Override
    public final void lose(int stake) {
        money -= stake;
    }

    @Override
    public final void win(int stake) {
        money += stake;
    }

    @Override
    public final int getMoney() {
        return money;
    }

    @Override
    public final String getName() {
        return name;
    }

}
