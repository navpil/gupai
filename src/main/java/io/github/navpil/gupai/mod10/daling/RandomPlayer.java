package io.github.navpil.gupai.mod10.daling;

import java.util.List;

public class RandomPlayer extends AbstractPlayer {

    public RandomPlayer(String name, int money) {
        super(name, money);
    }

    @Override
    public DaLingBet placeBets() {
        return new DaLingBet(List.of(1, 1, 1));
    }

    @Override
    public DaLingHand getHand(Table table) {
        return new DaLingHand(dominos.get(0), dominos.subList(1, 3), dominos.subList(3, 6));
    }
}
