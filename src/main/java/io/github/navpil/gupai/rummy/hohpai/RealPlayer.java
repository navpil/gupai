package io.github.navpil.gupai.rummy.hohpai;

import io.github.navpil.gupai.ConsoleInput;
import io.github.navpil.gupai.dominos.Domino;

import java.util.List;
import java.util.stream.Collectors;

public class RealPlayer extends AbstractPlayer {

    private final ConsoleInput consoleInput;
    private Hand winningHand;

    public RealPlayer(String name) {
        super(name);
        consoleInput = new ConsoleInput();
    }

    @Override
    public void give(Domino give) {
        dominos.add(give);
    }

    @Override
    public boolean hasWon() {
        final List<Hand> winningHands = getHands().stream().filter(Hand::isWinningHand).collect(Collectors.toList());
        if (winningHands.isEmpty()) {
            return false;
        }
        final int winningHandIndex = choice(winningHands, true, "Would you like to win with the following combinations?", consoleInput);
        if (winningHandIndex == 0) {
            return false;
        }
        winningHand= winningHands.get( winningHandIndex - 1);
        return true;
    }

    @Override
    public Hand getWinningHand() {
        return winningHand;
    }

    @Override
    public Domino getDiscard() {
        final int choice = choice(dominos, false, "Which domino would you like to discard?", consoleInput);
        return dominos.remove(choice - 1);
    }

    private static <T> int choice(List<T> choices, boolean zeroAllowed, String question, ConsoleInput consoleInput) {
        final StringBuilder sb = new StringBuilder(question).append("\n");
        if (zeroAllowed) {
            sb.append("0) None\n");
        }
        for (int i = 0; i < choices.size(); i++) {
            sb.append(i + 1).append(") ").append(choices.get(i)).append("\n");
        }
        return consoleInput.readInt(
                i -> i >= (zeroAllowed ? 0 : 1) && i <= choices.size(),
                sb.toString(),
                "Invalid input"
        );
    }
}
