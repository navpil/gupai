package io.github.navpil.gupai.shiwuhu;

import io.github.navpil.gupai.ChineseDominoSet;
import io.github.navpil.gupai.dominos.Domino;
import io.github.navpil.gupai.dominos.IDomino;
import io.github.navpil.gupai.DominoParser;
import io.github.navpil.gupai.fishing.tsungshap.RunManySimulations;
import io.github.navpil.gupai.jielong.Stats;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.function.Function;

public class ShiWuHuMain {

    public static void main(String[] args) {
        //[ShiWuHuHand[1]{supremes=[[4:2], [4:2], [2:1], [2:1]]hu=[[4:2], [4:2], [2:1], [2:1]], dominos=[[6:6], [6:6], [6:3], [6:3], [6:2], [5:3], [5:3], [5:2], [4:3], [3:1], [3:1], [4:1], [5:5], [3:3], [3:3], [2:2], [6:5]]}, ShiWuHuHand[2]{supremes=[]hu=[], dominos=[[6:6], [6:3], [5:4], [1:1], [6:2], [6:2], [5:3], [5:3], [4:4], [4:3], [4:2], [4:1], [3:2], [5:5], [5:5], [3:3], [2:2], [6:4], [6:1], [6:1], [6:1]]}, ShiWuHuHand[3]{supremes=[]hu=[], dominos=[[6:6], [6:3], [5:4], [1:1], [1:1], [6:2], [4:4], [4:4], [5:2], [5:2], [3:1], [4:1], [3:2], [3:3], [2:2], [2:2], [6:5], [6:4], [6:1], [5:1], [2:1]]}, ShiWuHuHand[4]{supremes=[[4:2], [2:1]]hu=[[4:2], [2:1]], dominos=[[5:4], [5:4], [1:1], [4:4], [5:2], [4:3], [4:3], [3:1], [4:1], [3:2], [3:2], [5:5], [6:5], [6:5], [6:4], [6:4], [5:1], [5:1], [5:1]]}]

        final ComputerPlayer.Strategy strategy = new ComputerPlayer.Strategy(true, true, false, false, false, true);
        final Function<String, Player> stringHandFunction = (s) -> new ComputerPlayer(s, strategy);

//        final WhoWon whoWon = runGame(true, true, ShiWuHuHand::new, 4, WinningCondition.GET_RID_ON_LAST_TRICK, getFastGameForGetRid());
//        System.out.println("Player won: " + whoWon.handName);
//        runTest(true, true, stringListHandBiFunction, 3, WinningCondition.TRICKS, getFastGameForGetRid());
        //Run single test
//        runTest(true, true, ShiWuHuHand::new, 4, WinningCondition.GET_RID);
        //Run tests with success rate
//        testSuccessRate(stringHandFunction);

        List<Player> players = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            players.add(new ComputerPlayer("Comp-" + i, strategy));
        }

        new RunManySimulations().runManySimulations(
                ChineseDominoSet.shiWuHuSet(),
                players,
                new RuleSet(true, RuleSet.WinningCondition.GET_RID_ON_LAST_TRICK),
                10,
                (dominos, players1, ruleSet, whoGoesFirst) -> statsFrom(runGame(false, dominos, ruleSet, players1,whoGoesFirst))
        );
    }

    private static void testSuccessRate(Function<String, Player> stringListHandBiFunction) {
        int success = 0;
        final int times = 10000;
        final HashMap<String, Integer> stats = new HashMap<>();

        int maxHu = 21;
        int maxHuRate = 0;

        final List<Domino> shiWuHuSet = ChineseDominoSet.shiWuHuSet();
        for (int i = 0; i < times; i++) {
            Collections.shuffle(shiWuHuSet);

            final ArrayList<Player> players = new ArrayList<>();
            for (int i1 = 0; i1 < 4; i1++) {
                players.add(stringListHandBiFunction.apply("" + i1));
            }

            //Try four different strategies, also combine them and check whether first person gets most of wins
            final WhoWon whoWon = runGame(false, shiWuHuSet, new RuleSet(true, RuleSet.WinningCondition.GET_RID_ON_LAST_TRICK), players, 0);
            if (whoWon.handName != null) {
                success++;
                if (!stats.containsKey(whoWon.handName)) {
                    stats.put(whoWon.handName, 0);
                }
                stats.put(whoWon.handName, stats.get(whoWon.handName) + 1);
            }
            if (whoWon.maxHu >= maxHu) {
                maxHuRate++;
            }
        }
        System.out.println("Success rate " + (100.0 * success / times) + "%");
        System.out.println("Winning rate: "+ stats);
        System.out.println("MaxHu rate: "+(100.0 * maxHuRate / times) + "%" );
    }

    //This will be an almost instant win for a first player with "GET_RID" rules, but not so for "TRICKS" rules.
    public static List<Domino> getFastGameForGetRid() {
        final ArrayList<Domino> realDominos = new ArrayList<>();
        String dominos = "[4:2], [4:2], [2:1], [2:1], [6:6], [6:6], [6:3], [6:3], [6:2], [5:3], [5:3], [5:2], [4:3], [3:1], [3:1], [4:1], [5:5], [3:3], [3:3], [2:2], [6:5], " +
                "[6:6], [6:3], [5:4], [1:1], [6:2], [6:2], [5:3], [5:3], [4:4], [4:3], [4:2], [4:1], [3:2], [5:5], [5:5], [3:3], [2:2], [6:4], [6:1], [6:1], [6:1], " +
                "[6:6], [6:3], [5:4], [1:1], [1:1], [6:2], [4:4], [4:4], [5:2], [5:2], [3:1], [4:1], [3:2], [3:3], [2:2], [2:2], [6:5], [6:4], [6:1], [5:1], [2:1], " +
                "[4:2], [2:1], [5:4], [5:4], [1:1], [4:4], [5:2], [4:3], [4:3], [3:1], [4:1], [3:2], [3:2], [5:5], [6:5], [6:5], [6:4], [6:4], [5:1], [5:1], [5:1]]  ";

        for (int i = 0; i < 84; i++) {
            final IDomino parse;
            try {
                parse = DominoParser.parse(dominos.substring(i * 7, (i + 1) * 7));
            } catch (Exception e) {
                throw e;
            }
            realDominos.add((Domino)parse);
        }
         return realDominos;

    }

    private static WhoWon runGame(boolean log, List<Domino> deck, RuleSet ruleSet, List<Player> players, int whoGoesFirst) {
        int currentHand = whoGoesFirst;
        int passes = 0;

        boolean breakOnEmptyLead = ruleSet.isBreakOnEmptyLead();
        final RuleSet.WinningCondition winningCondition = ruleSet.getWinningCondition();

        int leadsInARow = 0;
        String winningPlayer = null;

        List<Domino> lead = Collections.emptyList();

        if (log) System.out.println(players);
        int playersNo = players.size();
        int cardsPerPlayer = 84 / playersNo;

        for (int i = 0; i < playersNo; i++) {
            players.get(i).deal(deck.subList(i * cardsPerPlayer, (i + 1) * cardsPerPlayer));
        }

        play_cycle:
        while(leadsInARow < playersNo) {
            final Player player = players.get(currentHand);
            boolean noOneCouldBeatLastTrick = passes == playersNo - 1;
            if (lead.isEmpty()) {
                passes = 0;
                leadsInARow++;
                lead = player.lead();
                if (breakOnEmptyLead && lead.isEmpty()) {
                    if (log) System.out.println("Empty lead");
                    break;
                }
                if (log) System.out.println("Hand " + player.getName() + " leads with " + lead);
            } else if (noOneCouldBeatLastTrick) {
                if (winningCondition == RuleSet.WinningCondition.GET_RID_ON_LAST_TRICK && player.getLeftovers() <= (cardsPerPlayer - 15)) {
                    if (log) System.out.println("Hand " + player.getName() + " won");
                    winningPlayer = player.getName();
                    break play_cycle;
                }
                passes = 0;
                leadsInARow = 1;
                player.trick(lead);
                lead = player.lead();
                if (breakOnEmptyLead && lead.isEmpty()) {
                    if (log) System.out.println("Empty lead");
                    break;
                }
                if (log) System.out.println("Hand " + player.getName() + " takes a trick and leads with " + lead);
            } else {
                leadsInARow = 0;
                final List<Domino> beat = player.beat(lead);
                if (beat.isEmpty()) {
                    if (log) System.out.println("Hand "+ player.getName() +" passes");
                    passes++;
                } else {
                    passes = 0;
                    lead = beat;
                    if (log) System.out.println("Hand "+ player.getName() +" beats with " + beat);
                }
            }
            switch (winningCondition) {
                case NONE: break;
                case TRICKS:
                    if (player.getHu() >= 15) {
                        if (log) System.out.println("Hand " + player.getName() + " won");
                        winningPlayer = player.getName();
                        break play_cycle;
                    }
                    break;
                case GET_RID:
                    if (player.getLeftovers() <= (cardsPerPlayer - 15)) {
                        if (log) System.out.println("Hand " + player.getName() + " won " + (player));
                        winningPlayer = player.getName();
                        break play_cycle;
                    }
            }
            currentHand = (currentHand + 1) % playersNo;
        }
        int maxHu = 0;
        for (Player player : players) {
            if (log) System.out.println("Hand " + player.getName() + " got " + player.getHu() + "Hu");
            maxHu = Math.max(maxHu, player.getHu());
        }
        if (log) System.out.println(players);
        return new WhoWon(winningPlayer, maxHu);
    }

    private static Stats statsFrom(WhoWon whoWon) {
        final Stats stats = new Stats();
        stats.put(whoWon.getHandName(), whoWon.getMaxHu());
        return stats;
    }

    public static class WhoWon {
        private String handName;
        private int maxHu;

        public WhoWon(String handName, int maxHu) {
            this.handName = handName;
            this.maxHu = maxHu;
        }

        public String getHandName() {
            return handName;
        }

        public int getMaxHu() {
            return maxHu;
        }
    }


}
