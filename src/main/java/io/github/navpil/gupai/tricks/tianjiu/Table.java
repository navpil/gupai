package io.github.navpil.gupai.tricks.tianjiu;

import io.github.navpil.gupai.jielong.player.MutableInteger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Table {

    private final RuleSet ruleSet;
    private List<Trick> tricks = new ArrayList<>();
    private Map<Integer, MutableInteger> playerToTrickCount = new HashMap<>();

    public Table(RuleSet ruleSet) {
        this.ruleSet = ruleSet;
    }

    public void add(Trick trick) {
        tricks.add(trick);
        if (!playerToTrickCount.containsKey(trick.getTrickWinner())) {
            playerToTrickCount.put(trick.getTrickWinner(), new MutableInteger());
        }
        playerToTrickCount.get(trick.getTrickWinner()).add(trick.size());
    }

    public RuleSet getRuleSet() {
        return ruleSet;
    }

    public int getTrickCount(int current) {
        return playerToTrickCount.getOrDefault(current, new MutableInteger()).getCount();
    }

    public List<Trick> extractTricks() {
        final ArrayList<Trick> result = new ArrayList<>(tricks);
        tricks.clear();
        playerToTrickCount.clear();
        return result;
    }
}
