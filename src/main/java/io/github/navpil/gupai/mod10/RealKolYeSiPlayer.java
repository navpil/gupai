package io.github.navpil.gupai.mod10;

import io.github.navpil.gupai.ConsoleInput;
import io.github.navpil.gupai.dominos.Domino;

public class RealKolYeSiPlayer implements KolYeSiPlayer {

    private final ConsoleInput consoleInput;
    private final String name;
    private int money;

    public RealKolYeSiPlayer(String name, int money) {
        consoleInput = new ConsoleInput();
        this.name = name;
        this.money = money;

    }

    public int stake(Domino domino, int minStake, int maxStake) {
        System.out.println("You've got: " + domino);

        int maxPossibleStake = Math.min(money, maxStake);
        return consoleInput.readInt(
                (s) -> s >= minStake && s <= maxPossibleStake,
                "You have " + money + " left, what's your stake?",
                "Invalid stake, min: " + minStake + ", max: " + maxPossibleStake);
    }

    public int dominoCount(Domino d1, Domino d2) {
        return consoleInput.readInt(
                (s) -> s == 1 || s == 2,
                "How many tiles you want to pick, 1 or 2?",
                "Invalid input, try again");
    }

    public void lose(int stake) {
        money -= stake;
    }

    public void win(int stake) {
        money += stake;
    }

    public boolean stillHasMoney() {
        return money > 0;
    }

    public int getMoney() {
        return money;
    }

    @Override
    public String getName() {
        return name;
    }
}
