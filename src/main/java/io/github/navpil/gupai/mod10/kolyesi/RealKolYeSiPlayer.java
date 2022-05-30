package io.github.navpil.gupai.mod10.kolyesi;

import io.github.navpil.gupai.util.ConsoleInput;
import io.github.navpil.gupai.Domino;

public class RealKolYeSiPlayer extends PlayerSkeletal {

    private final ConsoleInput consoleInput;

    public RealKolYeSiPlayer(String name, int money) {
        super(name, money);
        consoleInput = new ConsoleInput();
    }

    @Override
    public int stake(Domino domino, int minStake, int maxStake) {
        System.out.println("You've got: " + domino);

        int maxPossibleStake = Math.min(getMoney(), maxStake);
        return consoleInput.readInt(
                (s) -> s >= minStake && s <= maxPossibleStake,
                "You have " + getMoney() + " left, what's your stake?",
                "Invalid stake, min: " + minStake + ", max: " + maxPossibleStake);
    }

    @Override
    public int dominoCount() {
        return consoleInput.readInt(
                (s) -> s == 1 || s == 2,
                "How many tiles you want to pick, 1 or 2?",
                "Invalid input, try again");
    }

}
