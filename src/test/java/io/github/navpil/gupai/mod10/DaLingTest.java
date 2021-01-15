package io.github.navpil.gupai.mod10;

import io.github.navpil.gupai.dominos.Domino;
import io.github.navpil.gupai.mod10.daling.ComputerPlayer;
import io.github.navpil.gupai.mod10.daling.DaLing;
import io.github.navpil.gupai.mod10.daling.DaLingHand;
import io.github.navpil.gupai.mod10.daling.RuleSet;
import io.github.navpil.gupai.mod10.daling.Table;
import org.assertj.core.api.Assertions;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;

public class DaLingTest {

    @Test
    public void testFindAbsoluteWinner() {

        final RuleSet macao = RuleSet.macaoWithBanker();

        final ComputerPlayer comp1 = new ComputerPlayer("Comp1", 100);
        final ComputerPlayer comp2 = new ComputerPlayer("Comp2", 100);

        final HashMap<String, DaLingHand> hands = new HashMap<>();
        hands.put("Comp1", new DaLingHand(Domino.of(21), Domino.ofList(64, 21), Domino.ofList(53, 65, 61)));
        hands.put("Comp2", new DaLingHand(Domino.of(33), Domino.ofList(33, 64), Domino.ofList(31, 61, 41)));

        final DaLing.WinnerResult absoluteWinner = DaLing.findAbsoluteWinner(new DaLing.PlayersGroup(List.of(comp1, comp2), -1), new Table(macao, Domino.ofList(22, 11), null), hands);
        Assertions.assertThat(absoluteWinner.isPushed()).isFalse();
        Assertions.assertThat(absoluteWinner.getWinner().getName()).isEqualTo("Comp1");
    }

}
