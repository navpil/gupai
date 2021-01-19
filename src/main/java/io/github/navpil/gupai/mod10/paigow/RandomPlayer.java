package io.github.navpil.gupai.mod10.paigow;

public class RandomPlayer extends AbstractPlayer {
    public RandomPlayer(String name, int money) {
        super(name, money);
    }

    @Override
    public Hand hand() {
        return new Hand(
                new PaiGowPair(dominos.subList(0, 2)),
                new PaiGowPair(dominos.subList(2, 4))
        );
    }
}
