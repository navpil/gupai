package io.navpil.github.zenzen.shiwuhu;

import io.navpil.github.zenzen.DominoParser;
import io.navpil.github.zenzen.dominos.IDomino;
import io.navpil.github.zenzen.dominos.Domino;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.function.BiFunction;

public class ShiWuHuMain {

    enum WinningCondition {
        TRICKS, GET_RID, GET_RID_ON_LAST_TRICK, NONE
    }

    public static void main(String[] args) {
        //[ShiWuHuHand[1]{supremes=[[4:2], [4:2], [2:1], [2:1]]hu=[[4:2], [4:2], [2:1], [2:1]], dominos=[[6:6], [6:6], [6:3], [6:3], [6:2], [5:3], [5:3], [5:2], [4:3], [3:1], [3:1], [4:1], [5:5], [3:3], [3:3], [2:2], [6:5]]}, ShiWuHuHand[2]{supremes=[]hu=[], dominos=[[6:6], [6:3], [5:4], [1:1], [6:2], [6:2], [5:3], [5:3], [4:4], [4:3], [4:2], [4:1], [3:2], [5:5], [5:5], [3:3], [2:2], [6:4], [6:1], [6:1], [6:1]]}, ShiWuHuHand[3]{supremes=[]hu=[], dominos=[[6:6], [6:3], [5:4], [1:1], [1:1], [6:2], [4:4], [4:4], [5:2], [5:2], [3:1], [4:1], [3:2], [3:3], [2:2], [2:2], [6:5], [6:4], [6:1], [5:1], [2:1]]}, ShiWuHuHand[4]{supremes=[[4:2], [2:1]]hu=[[4:2], [2:1]], dominos=[[5:4], [5:4], [1:1], [4:4], [5:2], [4:3], [4:3], [3:1], [4:1], [3:2], [3:2], [5:5], [6:5], [6:5], [6:4], [6:4], [5:1], [5:1], [5:1]]}]

        final GenericHand.Strategy strategy = new GenericHand.Strategy(true, true, false, false, false, true);
        final BiFunction<String, List<Domino>, Hand> stringListHandBiFunction = (s, t) -> new GenericHand(s, t, strategy);

//        final WhoWon whoWon = runGame(true, true, ShiWuHuHand::new, 4, WinningCondition.GET_RID_ON_LAST_TRICK, getFastGameForGetRid());
//        System.out.println("Player won: " + whoWon.handName);
//        runTest(true, true, stringListHandBiFunction, 3, WinningCondition.TRICKS, getFastGameForGetRid());
        //Run single test
//        runTest(true, true, ShiWuHuHand::new, 4, WinningCondition.GET_RID);
        //Run tests with success rate
        testSuccessRate(ShiWuHuHand::new);
    }

    private static void testSuccessRate(BiFunction<String, List<Domino>, Hand> stringListHandBiFunction) {
        int success = 0;
        final int times = 10000;
        final HashMap<String, Integer> stats = new HashMap<>();

        int maxHu = 21;
        int maxHuRate = 0;

        for (int i = 0; i < times; i++) {
            //Try four different strategies, also combine them and check whether first person gets most of wins
//            int maxHu = runTest(false, true, ShiWuHuHand2::new, 3, WinningCondition.TRICKS);
            final WhoWon whoWon = runGame(false, true, stringListHandBiFunction, 3, WinningCondition.NONE);
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

    private static void testSuccessRate_old(BiFunction<String, List<Domino>, Hand> stringListHandBiFunction) {
        int success = 0;
        final int times = 10000;
        for (int i = 0; i < times; i++) {
            //Try four different strategies, also combine them and check whether first person gets most of wins
//            int maxHu = runTest(false, true, ShiWuHuHand2::new, 3, WinningCondition.TRICKS);
            int maxHu = runGame(false, true, stringListHandBiFunction, 3, WinningCondition.TRICKS).getMaxHu();
            if (maxHu >= 15) {
                success++;
            }
        }
        System.out.println("Success rate " + (100.0 * success / times) + "%");
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

    private static WhoWon runGame(boolean log, boolean breakOnEmptyLead, BiFunction<String, List<Domino>, Hand> producer, int playersNo, WinningCondition winningCondition) {
        final List<Domino> shiWuHuSet = ShiWuHuSet.create();
        Collections.shuffle(shiWuHuSet);

        return runGame(log, breakOnEmptyLead, producer, playersNo, winningCondition, shiWuHuSet);
    }

    private static WhoWon runGame(boolean log, boolean breakOnEmptyLead, BiFunction<String, List<Domino>, Hand> producer, int playersNo, WinningCondition winningCondition, List<Domino> shiWuHuSet) {
        final ArrayList<Hand> hands = new ArrayList<>();
        int cardsPerPlayer = 84 / playersNo;
        for (int i = 0; i < playersNo; i++) {
            hands.add(producer.apply(""+(i+1), shiWuHuSet.subList(i * cardsPerPlayer, (i + 1) * cardsPerPlayer)));
        }

        return runGame(log, breakOnEmptyLead, playersNo, winningCondition, hands, cardsPerPlayer);
    }

    private static WhoWon runGame(boolean log, boolean breakOnEmptyLead, int playersNo, WinningCondition winningCondition, ArrayList<Hand> hands, int cardsPerPlayer) {
        int currentHand = 0;
        int passes = 0;

        int leadsInARow = 0;
        String winningPlayer = null;

        List<Domino> lead = Collections.emptyList();

        if (log) System.out.println(hands);

        play_cycle:
        while(leadsInARow < playersNo) {
            final Hand hand = hands.get(currentHand);
            if (lead.isEmpty()) {
                passes = 0;
                leadsInARow++;
                lead = hand.lead();
                if (breakOnEmptyLead && lead.isEmpty()) {
                    if (log) System.out.println("Empty lead");
                    break;
                }
                if (log) System.out.println("Hand " + hand.getName() + " leads with " + lead);
            } else if (passes == playersNo - 1) {
                if (winningCondition == WinningCondition.GET_RID_ON_LAST_TRICK && hand.getLeftovers() <= (cardsPerPlayer - 15)) {
                    if (log) System.out.println("Hand " + hand.getName() + " won");
                    winningPlayer = hand.getName();
                    break play_cycle;
                }
                passes = 0;
                leadsInARow = 1;
                hand.trick(lead);
                lead = hand.lead();
                if (breakOnEmptyLead && lead.isEmpty()) {
                    if (log) System.out.println("Empty lead");
                    break;
                }
                if (log) System.out.println("Hand " + hand.getName() + " takes a trick and leads with " + lead);
            } else {
                leadsInARow = 0;
                final List<Domino> beat = hand.beat(lead);
                if (beat.isEmpty()) {
                    if (log) System.out.println("Hand "+hand.getName() +" passes");
                    passes++;
                } else {
                    passes = 0;
                    lead = beat;
                    if (log) System.out.println("Hand "+hand.getName() +" beats with " + beat);
                }
            }
            switch (winningCondition) {
                case NONE: break;
                case TRICKS:
                    if (hand.getHu() >= 15) {
                        if (log) System.out.println("Hand " + hand.getName() + " won");
                        winningPlayer = hand.getName();
                        break play_cycle;
                    }
                    break;
                case GET_RID:
                    if (hand.getLeftovers() <= (cardsPerPlayer - 15)) {
                        if (log) System.out.println("Hand " + hand.getName() + " won " + (hand));
                        winningPlayer = hand.getName();
                        break play_cycle;
                    }
            }
            currentHand = (currentHand + 1) % playersNo;
        }
        int maxHu = 0;
        for (Hand hand : hands) {
            if (log) System.out.println("Hand " + hand.getName() + " got " + hand.getHu() + "Hu");
            maxHu = Math.max(maxHu, hand.getHu());
        }
        if (log) System.out.println(hands);
        return new WhoWon(winningPlayer, maxHu);
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
