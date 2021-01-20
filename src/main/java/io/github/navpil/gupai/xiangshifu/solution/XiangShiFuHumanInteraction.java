package io.github.navpil.gupai.xiangshifu.solution;

import io.github.navpil.gupai.ChineseDominoSet;
import io.github.navpil.gupai.xiangshifu.Combination;
import io.github.navpil.gupai.util.ConsoleInput;
import io.github.navpil.gupai.xiangshifu.Move;
import io.github.navpil.gupai.xiangshifu.Triplet;

import java.util.LinkedList;
import java.util.List;

public class XiangShiFuHumanInteraction {

    public static void main(String[] args) {

        final ConsoleInput consoleInput = new ConsoleInput();
        final List<Triplet> triplets = ChineseDominoSet.random10Triplets();
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
        } while (triplets.stream().allMatch(triplet -> triplet.getCombination() != Combination.none));

    }

}
