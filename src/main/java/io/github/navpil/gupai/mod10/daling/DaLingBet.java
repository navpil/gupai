package io.github.navpil.gupai.mod10.daling;

import java.util.List;

public class DaLingBet {

    private final List<Integer> bets;

    public DaLingBet(List<Integer> bets) {
        if (bets.size() != 3) {
            throw new IllegalArgumentException("Should have exactly 3 bets");
        }
        this.bets = bets;
    }

    public Integer getBet(int i) {
        return bets.get(i);
    }

    public Integer getTotal() {
        return bets.stream().mapToInt(b -> b).sum();
    }


}
