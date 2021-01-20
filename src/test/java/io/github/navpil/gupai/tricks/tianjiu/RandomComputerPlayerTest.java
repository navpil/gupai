package io.github.navpil.gupai.tricks.tianjiu;

import io.github.navpil.gupai.Domino;
import org.junit.Test;

import java.util.Collection;

import static org.assertj.core.api.Assertions.*;

public class RandomComputerPlayerTest {

    @Test
    public void playerShouldNotBeAbleToBeatHigherLead() {
        final RandomComputerPlayer player = new RandomComputerPlayer("Random");
        player.deal(Domino.ofList(55,55));
        player.showTable(new Table(RuleSet.classicTianJiu()));

        final Collection<Domino> beat = player.beat(Domino.ofList(11, 11));
        assertThat(beat).isEmpty();
    }

    @Test
    public void testMilitaryTilesBeating() {

        final RuleSet ruleSet = RuleSet.classicTianJiu();
        assertThat(new HandHelper(ruleSet).canBeat(Domino.ofList(63), Domino.ofList(62))).isFalse();
        assertThat(new HandHelper(ruleSet).canBeat(Domino.ofList(62), Domino.ofList(63))).isTrue();
    }
}
