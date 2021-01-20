package io.github.navpil.gupai.fishing.tsungshap;

import io.github.navpil.gupai.util.ConsoleInput;
import io.github.navpil.gupai.Domino;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

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

        final ArrayList<TsungShapMove> moves = new ArrayList<>(List.of(
                new TsungShapMove(TsungShapMove.Type.DISCARD, TsungShapMove.Side.LEFT, domino),
                new TsungShapMove(TsungShapMove.Type.DISCARD, TsungShapMove.Side.RIGHT, domino)
        ));
        if (row.size() > 0) {
            moves.addAll(List.of(
                    new TsungShapMove(TsungShapMove.Type.PAIR, TsungShapMove.Side.LEFT, domino),
                    new TsungShapMove(TsungShapMove.Type.PAIR, TsungShapMove.Side.RIGHT, domino)
            ));
        }
        if (row.size() > 1) {
            moves.addAll(List.of(
                    new TsungShapMove(TsungShapMove.Type.TRIPLET, TsungShapMove.Side.LEFT, domino),
                    new TsungShapMove(TsungShapMove.Type.TRIPLET, TsungShapMove.Side.RIGHT, domino),
                    new TsungShapMove(TsungShapMove.Type.TRIPLET, TsungShapMove.Side.BOTH, domino)
            ));
        }
        return consoleInput.choice(moves, false, "Row is: \n" + row + "\n" +
                "You were given this domino: " + domino + ", which move you want to make?\n");
    }

    @Override
    public void showTable(TsungShapTable table) {
    }
}
