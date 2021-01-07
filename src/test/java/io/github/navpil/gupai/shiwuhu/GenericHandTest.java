package io.github.navpil.gupai.shiwuhu;

import io.github.navpil.gupai.dominos.Domino;
import org.junit.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

public class GenericHandTest {

    @Test
    public void testBeatingMultipleTimes() {
        final ComputerPlayer hand = new ComputerPlayer("TestHand", Domino.ofList(66, 66, 66, 66, 22), new ComputerPlayer.Strategy(true, true, false, false, false, true));

        for (int i = 0; i < 4; i++) {
            final List<Domino> beat = hand.beat(Domino.ofList(11));
            assertThat(beat.size()).describedAs("Beat Earth by Heaven for " + i + " time").isEqualTo(1);
        }

    }

    @Test
    public void testBeatingComboMultipleTimes() {
        final ComputerPlayer hand = new ComputerPlayer("TestHand", Domino.ofList(66, 66, 66, 66, 22), new ComputerPlayer.Strategy(true, true, false, false, false, true));

        for (int i = 0; i < 2; i++) {
            final List<Domino> beat = hand.beat(Domino.ofList(11, 11));
            assertThat(beat.size()).describedAs("Beat Earth by Heaven for " + i + " time").isEqualTo(2);
        }

    }
}