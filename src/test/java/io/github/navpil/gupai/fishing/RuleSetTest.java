package io.github.navpil.gupai.fishing;

import io.github.navpil.gupai.Domino;
import io.github.navpil.gupai.fishing.tiuu.RuleSet;
import io.github.navpil.gupai.util.HashBag;
import org.junit.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class RuleSetTest {

    @Test
    public void canCatch() {
        final RuleSet culin = RuleSet.culin();
        assertThat(culin.canCatch(Domino.of(3, 3), Domino.of(3, 3))).isTrue();
    }

    @Test
    public void testTianJiu() {
        //Mixed
        assertThat(RuleSet.mixed().calculatePoints(Domino.ofList(63, 66, 66))).isEqualTo(90);
        assertThat(RuleSet.mixed().calculatePoints(Domino.ofList(63, 54, 54))).isEqualTo(30);

        //Alone...
        assertThat(RuleSet.alone().calculatePoints(Domino.ofList(63, 66, 66))).isEqualTo(30);
        assertThat(RuleSet.alone().calculatePoints(Domino.ofList(63, 54, 66))).isEqualTo(90);
    }

    @Test
    public void testEarthEight() {
        //Mixed
        assertThat(RuleSet.mixed().calculatePoints(Domino.ofList(53, 11, 11))).isEqualTo(60);
        assertThat(RuleSet.mixed().calculatePoints(Domino.ofList(53, 62, 62))).isEqualTo(30);

        //Alone...
        assertThat(RuleSet.alone().calculatePoints(Domino.ofList(53, 11, 11))).isEqualTo(30);
        assertThat(RuleSet.alone().calculatePoints(Domino.ofList(53, 62, 11))).isEqualTo(60);
    }

    @Test
    public void testJun() {
        final List<Integer> integers = List.of(55, 64, 65);
        for (int i : integers) {
            assertThat(RuleSet.mixed().calculatePoints(Domino.ofList(i))).isEqualTo(10);
            assertThat(RuleSet.mixed().calculatePoints(Domino.ofList(i, i))).isEqualTo(20);
            assertThat(RuleSet.mixed().calculatePoints(Domino.ofList(i, i, i))).isEqualTo(30);
            assertThat(RuleSet.mixed().calculatePoints(Domino.ofList(i, i, i, i))).isEqualTo(120);
        }
    }

    @Test
    public void testAllJunBonus() {
        final HashBag<Domino> juns = new HashBag<>();
        final List<Integer> integers = List.of(55, 64, 65);
        for (int i : integers) {
            juns.addAll(Domino.ofList(i, i, i, i));
        }
        assertThat(RuleSet.mixed().calculatePoints(juns)).isEqualTo((120 * 3) + 300);
    }
}
