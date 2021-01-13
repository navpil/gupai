package io.github.navpil.gupai.fishing.tsungshap.fourtiles;

import io.github.navpil.gupai.ConsoleInput;
import io.github.navpil.gupai.dominos.Domino;
import io.github.navpil.gupai.fishing.tsungshap.TsungShapMove;

import java.util.LinkedList;
import java.util.List;

import static io.github.navpil.gupai.fishing.tsungshap.TsungShapMove.Type.*;
import static io.github.navpil.gupai.fishing.tsungshap.TsungShapMove.Side.*;

public class HumanPlayer extends AbstractPlayer {

    private final ConsoleInput consoleInput;

    public HumanPlayer(String name) {
        super(name);
        consoleInput = new ConsoleInput();
    }

    @Override
    public Domino chooseSingleDiscard() {
        return withDominoRemoved(consoleInput.choice(dominos, false, "Which domino would you like to discard?"));
    }

    @Override
    public TsungShapMove chooseMove(LinkedList<Domino> row) {

        Domino domino;
        TsungShapMove.Type type;
        TsungShapMove.Side side;
        do {
            domino = consoleInput.choice(dominos, false, "Which domino you'd like to play for a row " + row + "?");
            type = consoleInput.choice(List.of(DISCARD, PAIR, TRIPLET), false, "What would you like to do with a domino?");
            if (type == TRIPLET) {
                side = consoleInput.choice(List.of(LEFT, RIGHT, BOTH), false, "Which triplet would you like to form?");
            } else {
                side = consoleInput.choice(List.of(LEFT, RIGHT), false, "Where would you like to put it?");
            }

        } while (!isValidMove(domino, type, side, row));
        return withDominoRemoved(new TsungShapMove(type, side, domino));
    }

    private boolean isValidMove(Domino domino, TsungShapMove.Type type, TsungShapMove.Side side, LinkedList<Domino> row) {
        if (row.size() < 2 && type == TRIPLET) {
            return false;
        }
        return handHelper.isValidMove(new TsungShapMove(type, side, domino), row);
    }


}
