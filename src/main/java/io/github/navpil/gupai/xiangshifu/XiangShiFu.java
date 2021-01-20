package io.github.navpil.gupai.xiangshifu;

import io.github.navpil.gupai.ChineseDominoSet;
import io.github.navpil.gupai.CombinationType;
import io.github.navpil.gupai.XuanHePaiPu;
import io.github.navpil.gupai.util.ConsoleInput;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Supplier;

public class XiangShiFu {

    public enum GameType {
        TEN_GROUPS,
        EIGHT_GROUPS
    }

    public enum GroupsType {
        CLASSIC,
        MODERN
    }

    public static void main(String[] args) {
        final ConsoleInput consoleInput = new ConsoleInput();
        final GameType gameType = consoleInput.choice(Arrays.asList(GameType.values()), false, "Choose a game type");
        final GroupsType groupsType = consoleInput.choice(Arrays.asList(GroupsType.values()), false, "Choose a groups type");

        List<Triplet> triplets = prepareTheGame(gameType, groupsType);

        runSimulation(triplets);

    }

    private static List<Triplet> prepareTheGame(GameType gameType, GroupsType groupsType) {
        final XuanHePaiPu xuanHePaiPu = groupsType == GroupsType.CLASSIC ? XuanHePaiPu.xiangShiFu() : XuanHePaiPu.xiangShiFuModern();
        XiangShiFuRules.changeRules(xuanHePaiPu);
        final DeadSets deadSets = new DeadSets();

        Supplier<List<Triplet>> supplier = gameType == GameType.TEN_GROUPS ? ChineseDominoSet::random10Triplets : ChineseDominoSet::random8Triplets;

        List<Triplet> triplets;
        if (groupsType == GroupsType.CLASSIC) {
            do {
                triplets = supplier.get();
            } while (triplets.stream().map(Triplet::asBag).noneMatch(deadSets::isDeadSet));
        } else {
            triplets = supplier.get();
        }
        return triplets;
    }

    public static void runSimulation(List<Triplet> triplets) {
        final ConsoleInput consoleInput = new ConsoleInput();
        LinkedList<Move> moves = new LinkedList<>();

        game_loop:
        do {
            Move move = null;

            move_loop:
            do {
                final List<Triplet> twoTriplets = consoleInput.multiChoice(triplets, true, "Which triplets you want to use for exchange", 2);
                if (twoTriplets.isEmpty()) {
                    final String choice = consoleInput.choice(List.of("UNDO", "CONTINUE", "QUIT"), false, "What do you want to do?");
                    switch (choice) {
                        case "QUIT":
                            break game_loop;
                        case "UNDO":
                            if (moves.isEmpty()) {
                                System.out.println("Nothing to undo");
                            } else {
                                moves.removeLast().undo();
                            }
                        case "CONTINUE":
                            continue move_loop;
                    }
                }
                int first = consoleInput.readInt(i -> i > 0 && i <= 3, "Choose a tile from " + twoTriplets.get(0), "Invalid input (should only be 1,2,3)");
                int second = consoleInput.readInt(i -> i > 0 && i <= 3, "Choose a tile from  "+ twoTriplets.get(1), "Invalid input (should only be 1,2,3)");

                move = new Move(twoTriplets.get(0), first, twoTriplets.get(1), second);
                if (!move.valid()) {
                    System.out.println("Move is invalid");
                }
            } while (!(move != null && move.valid()));
            move.execute();
            moves.add(move);
        } while (triplets.stream().allMatch(triplet -> triplet.getCombination() != CombinationType.none));
    }

}
