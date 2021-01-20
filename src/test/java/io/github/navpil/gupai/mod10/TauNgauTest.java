package io.github.navpil.gupai.mod10;

import io.github.navpil.gupai.Domino;
import io.github.navpil.gupai.mod10.taungau.ComputerPlayer;
import org.junit.Test;

import java.util.Collection;

import static org.assertj.core.api.Assertions.assertThat;

public class TauNgauTest {

    @Test
    public void testComputerPlayerWillNotDiscardSupreme() {
        final ComputerPlayer player = new ComputerPlayer("Comp", 100);
        player.deal(Domino.ofList(42, 21, 22, 55, 33));
        final Collection<Domino> discard = player.discard();
        assertThat(discard).containsExactlyInAnyOrder(Domino.ofList(33, 22, 55).toArray(new Domino[0]));
    }

}
