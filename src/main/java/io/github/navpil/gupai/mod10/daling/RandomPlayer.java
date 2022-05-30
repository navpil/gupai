package io.github.navpil.gupai.mod10.daling;

public class RandomPlayer extends AbstractPlayer {

    public RandomPlayer(String name, int money) {
        super(name, money);
    }

    @Override
    public DaLingHand getHand(Table table) {
        return new DaLingHand(dominos.get(0), dominos.subList(1, 3), dominos.subList(3, 6));
    }
}
