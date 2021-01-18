package io.github.navpil.gupai.rummy.hohpai;

import io.github.navpil.gupai.dominos.Domino;

import java.util.Random;

public class RandomPlayer extends AbstractPlayer {

    private final Random random;

    public RandomPlayer(String name) {
        super(name);
        random = new Random();
    }

    @Override
    public void give(Domino give) {
        dominos.add(give);
    }

    @Override
    public Hand getWinningHand() {
        return getHands()
                .stream()
                .filter(Hand::isWinningHand)
                .findAny()
                .orElse(null);
    }


    @Override
    public Domino discard() {
//        final Collection<Hand> hands = getHands();
//        if (hands.isEmpty()) {
            return dominos.remove(random.nextInt(dominos.size()));
//        } else {
//            final Collection<Domino> deadwood = hands.iterator().next().getDeadwood();
//            final int count = random.nextInt(deadwood.size());
//            final Iterator<Domino> iterator = deadwood.iterator();
//            for (int i = 0; i < count; i++) {
//                iterator.next();
//            }
//            final Domino next = iterator.next();
//            dominos.remove(next);
//            return next;
//
//        }
    }
}
