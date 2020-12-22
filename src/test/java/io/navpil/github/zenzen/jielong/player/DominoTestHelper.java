package io.navpil.github.zenzen.jielong.player;

import io.navpil.github.zenzen.dominos.Domino;
import io.navpil.github.zenzen.jielong.Move;

import java.util.List;
import java.util.stream.Collectors;

public class DominoTestHelper {

    public static List<Move> toMoves(List<Domino> dominos) {
        return dominos.stream().map(d -> new Move(0, d.getPips()[0], d.getPips()[1])).collect(Collectors.toList());
    }

}
