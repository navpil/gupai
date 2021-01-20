package io.github.navpil.gupai.tricks.shiwuhu;

import io.github.navpil.gupai.Domino;
import io.github.navpil.gupai.tricks.shiwuhu.ComputerPlayer;
import org.junit.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

public class GenericHandTest {

    @Test
    public void testBeatingMultipleTimes() {
        final ComputerPlayer hand = new ComputerPlayer("TestHand", new ComputerPlayer.Strategy(true, true, false, false, false, true));
        hand.deal(Domino.ofList(66, 66, 66, 66, 22));

        for (int i = 0; i < 4; i++) {
            final List<Domino> beat = hand.beat(Domino.ofList(11));
            assertThat(beat.size()).describedAs("Beat Earth by Heaven for " + i + " time").isEqualTo(1);
        }

    }

    @Test
    public void testBeatingComboMultipleTimes() {
        final ComputerPlayer hand = new ComputerPlayer("TestHand", new ComputerPlayer.Strategy(true, true, false, false, false, true));
        hand.deal(Domino.ofList(66, 66, 66, 66, 22));
        for (int i = 0; i < 2; i++) {
            final List<Domino> beat = hand.beat(Domino.ofList(11, 11));
            assertThat(beat.size()).describedAs("Beat Earth by Heaven for " + i + " time").isEqualTo(2);
        }

    }
}
