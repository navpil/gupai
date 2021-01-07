package io.github.navpil.gupai.jielong;

import io.github.navpil.gupai.dominos.Domino;
import io.github.navpil.gupai.jielong.player.PriorityPlayer;
import org.junit.Test;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class CeDengRuleSetTest {

    @Test
    public void resolvePoints() {
        final CeDengRuleSet ceDengRuleSet = new CeDengRuleSet(7, CeDengRuleSet.Penalty.SIX_HEAD_SEVEN_TAIL);
        final List<EndPlayer> players = List.of(
                new EndPlayer("Comp1", Domino.ofList(11)), //2 points
                new EndPlayer("Comp2", Domino.ofList(11,11,11,11,11,11,11)), //14 points, penalty x2
                new EndPlayer("Comp3", Domino.ofList()),//Pass bonus x2
                new EndPlayer("Comp4", Domino.ofList(11,11,11,11,11,11,22)) //16 points, penalty x2
        );
        final RuleSet.WinningStats winningStats = ceDengRuleSet.resolvePoints(null, players, 2);
        assertThat(winningStats.getWinner()).isEqualTo("Comp3");
        /*
         * 1-2 -> + 12x2
         * 1-3 -> - 2x2
         * 1-4 -> + 14x2
         *
         * 2-3 -> - 14x4
         * 2-4 -> + 2
         *
         * 3-4 -> + 16x4
         */
        final HashMap<String, Integer> points = winningStats.getPoints();
        /*----------------------------------------1        2      3      4*/
        assertThat(points.get("Comp1")).isEqualTo(       +12*2  -2*2   +14*2 );
        assertThat(points.get("Comp2")).isEqualTo(-12*2         -14*4  +2    );
        assertThat(points.get("Comp3")).isEqualTo(+2*2   +14*4         +16*4 );
        assertThat(points.get("Comp4")).isEqualTo(-14*2  -2     -16*4        );
    }

    @Test
    public void resolvePointsWhenPointsAreTheSame() {
        final CeDengRuleSet ceDengRuleSet = new CeDengRuleSet(7, CeDengRuleSet.Penalty.SIX_HEAD_SEVEN_TAIL);
        final List<EndPlayer> players = List.of(
                new EndPlayer("Comp1", Domino.ofList(11)),
                new EndPlayer("Comp2", Domino.ofList(11)),
                new EndPlayer("Comp3", Domino.ofList(11)),
                new EndPlayer("Comp4", Domino.ofList(11))
        );
        final RuleSet.WinningStats winningStats = ceDengRuleSet.resolvePoints(null, players, 2);
        assertThat(winningStats.getWinner()).isEqualTo("Comp3");
        assertThatAllPointsAreZero(winningStats.getPoints());
    }

    @Test
    public void resolvePointsWhenRedeal() {
        final CeDengRuleSet ceDengRuleSet = new CeDengRuleSet(7, CeDengRuleSet.Penalty.SIX_HEAD_SEVEN_TAIL);
        final List<EndPlayer> players = redeal();
        final RuleSet.WinningStats winningStats = ceDengRuleSet.resolvePoints(null, players, 2);
        assertThat(winningStats.getWinner()).isEqualTo("Comp3");
        assertThatAllPointsAreZero(winningStats.getPoints());
    }

    @Test
    public void resolvePointsWhenExtraordinaryWin() {
        final CeDengRuleSet ceDengRuleSet = new CeDengRuleSet(7, CeDengRuleSet.Penalty.SIX_HEAD_SEVEN_TAIL);
        final List<EndPlayer> players = extraWinner();
        final RuleSet.WinningStats winningStats = ceDengRuleSet.resolvePoints(null, players, 2);
        assertThat(winningStats.getWinner()).isEqualTo("Comp1");
        assertThat(winningStats.getPoints().get("Comp1")).isEqualTo(3 * 8 * 3 * 2);
    }

    @Test
    public void eightDoublesStopTheGame() {
        assertThat(new CeDengRuleSet(7, CeDengRuleSet.Penalty.SIX_HEAD_SEVEN_TAIL).continueTheGame(extraWinner())).isFalse();
    }

    @Test
    public void sevenDoublesStopTheGame() {
        assertThat(new CeDengRuleSet(7, CeDengRuleSet.Penalty.SIX_HEAD_SEVEN_TAIL).continueTheGame(redeal())).isFalse();
    }

    private List<EndPlayer> redeal() {
        return List.of(
                new EndPlayer("Comp1", Domino.ofList(11, 11, 22, 22, 33, 33, 44, 32)),
                new EndPlayer("Comp2", Domino.ofList(12, 12, 12, 12, 12, 12, 12, 12)),
                new EndPlayer("Comp3", Domino.ofList(11, 12, 12, 12, 12, 12, 12, 12)),
                new EndPlayer("Comp4", Domino.ofList(11, 12, 12, 12, 12, 12, 12, 12))
        );
    }

    private List<EndPlayer> extraWinner() {
        return List.of(
                    new EndPlayer("Comp1", Domino.ofList(11, 11, 22, 22, 33, 33, 44, 44)),
                    new EndPlayer("Comp2", Domino.ofList(12, 12, 12, 12, 12, 12, 12, 12)),
                    new EndPlayer("Comp3", Domino.ofList(12, 12, 12, 12, 12, 12, 12, 12)),
                    new EndPlayer("Comp4", Domino.ofList(12, 12, 12, 12, 12, 12, 12, 12))
            );
    }

    private void assertThatAllPointsAreZero(HashMap<String, Integer> points) {
        for (Integer value : points.values()) {
            assertThat(value).isEqualTo(0);
        }
    }

    private static class EndPlayer extends PriorityPlayer {

        private final Collection<Domino> leftovers;

        public EndPlayer(String name, Collection<Domino> leftovers) {
            super(name, null);
            this.leftovers = leftovers;
        }

        @Override
        public Collection<Domino> leftOvers() {
            return leftovers;
        }
    }
}