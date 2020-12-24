package io.navpil.github.zenzen.mod10;

import io.navpil.github.zenzen.ConsoleInput;
import io.navpil.github.zenzen.dominos.Domino;

public class RealKolYeSiPlayer implements KolYeSiPlayer {

    private final ConsoleInput consoleInput;
    private final String name;
    private final int order;
    private final int initialMoney;
    private int money;
    private int stake;

    public RealKolYeSiPlayer(String name, int order, int money) {
        consoleInput = new ConsoleInput();
        this.name = name;
        this.initialMoney = money;
        this.money = money;
        this.order = order;

    }

    public int stake(Domino domino, int minStake, int maxStake) {
        System.out.println("You've got: " + domino);

        int maxPossibleStake = Math.min(money, maxStake);
        stake = consoleInput.readInt(
                (s) -> s >= minStake && s <= maxPossibleStake,
                "You have " + money + " left, what's your stake?",
                "Invalid stake, min: " + minStake + ", max: " + maxPossibleStake);
        return stake;
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
