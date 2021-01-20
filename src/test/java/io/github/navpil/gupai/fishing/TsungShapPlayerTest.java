package io.github.navpil.gupai.fishing;

import io.github.navpil.gupai.fishing.tsungshap.TsungShapComputerPlayer;
import io.github.navpil.gupai.fishing.tsungshap.TsungShapMove;
import io.github.navpil.gupai.fishing.tsungshap.TsungShapRuleSet;
import io.github.navpil.gupai.DominoParser;
import io.github.navpil.gupai.Domino;
import io.github.navpil.gupai.fishing.tsungshap.TsungShapPlayer;
import io.github.navpil.gupai.fishing.tsungshap.TsungShapTable;
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


        final TsungShapPlayer p = new TsungShapComputerPlayer("Comp-1");
        p.showTable(tsungShapTable);
        Assertions.assertThat(p.chooseMove(Domino.of(51), row)).isEqualTo(new TsungShapMove(TsungShapMove.Type.PAIR, TsungShapMove.Side.RIGHT, Domino.of(51)));

    }

}
