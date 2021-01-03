package io.navpil.github.zenzen.fishing;

import io.navpil.github.zenzen.DominoParser;
import io.navpil.github.zenzen.dominos.Domino;
import io.navpil.github.zenzen.fishing.tsungshap.TsungShapMove;
import io.navpil.github.zenzen.fishing.tsungshap.TsungShapPlayer;
import io.navpil.github.zenzen.fishing.tsungshap.TsungShapRuleSet;
import io.navpil.github.zenzen.fishing.tsungshap.TsungShapTable;
import org.assertj.core.api.Assertions;
import org.junit.Test;

import java.util.LinkedList;
import java.util.List;

public class TsungShapPlayerTest {

    @Test
    public void catchPairTest() {

        final List<Domino> d = DominoParser.parseList("[6:5], [6:1], [6:3], [3:3], [5:1], [5:1]");
        final LinkedList<Domino> row = new LinkedList<>(d);

        final TsungShapTable tsungShapTable = new TsungShapTable(TsungShapRuleSet.classic());


        final TsungShapPlayer p = new TsungShapPlayer("Comp-1");
        p.showTable(tsungShapTable);
        Assertions.assertThat(p.chooseMove(Domino.of(51), row)).isEqualTo(new TsungShapMove(TsungShapMove.Type.PAIR, TsungShapMove.Side.RIGHT, Domino.of(51)));

    }

}