package io.github.navpil.gupai.fishing.tsungshap;

import io.github.navpil.gupai.ConsoleInput;
import io.github.navpil.gupai.dominos.Domino;

import java.util.LinkedList;

public class TsungShapRealPlayer implements TsungShapPlayer {

    private final String name;
    private final ConsoleInput consoleInput;

    public TsungShapRealPlayer(String name) {
        this.name = name;
        consoleInput = new ConsoleInput();
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public TsungShapMove chooseMove(Domino domino, LinkedList<Domino> row) {

        String prompt = "Row is: \n" + row + "\n" +
                "You were given this domino: " + domino + ", which move you want to make?\n" +
                "1) Discard to the left\n" +
                "2) Discard to the right\n" +
                "3) Pair with left side\n" +
                "4) Pair with right side\n" +
                "5) Triplet with left side\n" +
                "6) Triple with right side\n" +
                "7) Triplet with both sides\n";
        final int moveType = consoleInput.readInt(
                (i) -> i >= 1 && i <= 7,
                prompt,
                "Invalid input"
        );
        switch (moveType) {
            case 1: return new TsungShapMove(TsungShapMove.Type.DISCARD, TsungShapMove.Side.LEFT, domino);
            case 2: return new TsungShapMove(TsungShapMove.Type.DISCARD, TsungShapMove.Side.RIGHT, domino);
            case 3: return new TsungShapMove(TsungShapMove.Type.PAIR, TsungShapMove.Side.LEFT, domino);
            case 4: return new TsungShapMove(TsungShapMove.Type.PAIR, TsungShapMove.Side.RIGHT, domino);
            case 5: return new TsungShapMove(TsungShapMove.Type.TRIPLET, TsungShapMove.Side.LEFT, domino);
            case 6: return new TsungShapMove(TsungShapMove.Type.TRIPLET, TsungShapMove.Side.RIGHT, domino);
            case 7: return new TsungShapMove(TsungShapMove.Type.TRIPLET, TsungShapMove.Side.BOTH, domino);
        }
        throw new IllegalStateException("Move type undefined: " + moveType);
    }

    @Override
    public void showTable(TsungShapTable table) {
    }
}
