package io.github.navpil.gupai.mod10;

import io.github.navpil.gupai.dominos.Domino;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class Mod10RuleTest {


    //KolYeSi
    @Test
    public void kolYeSiNonMod10() {
        final Mod10Rule mod10Rule = Mod10Rule.kolYeSi();
        final Mod10Rule.Points points = mod10Rule.getPoints(Domino.ofList(42, 33));
        assertThat(points.isMod10()).isFalse();
        assertThat(points.getMax()).isEqualTo(2);
    }

    @Test
    public void kolYeSiMod10() {
        final Mod10Rule mod10Rule = Mod10Rule.kolYeSi();
        final Mod10Rule.Points points = mod10Rule.getPoints(Domino.ofList(42, 31));
        assertThat(points.isMod10()).isTrue();
        assertThat(points.getMax()).isEqualTo(0);
    }

    //DaLing
    @Test
    public void testDaLing() {
        final Mod10Rule mod10Rule = Mod10Rule.daLing();
        assertThat(mod10Rule.getPoints(Domino.ofList(42, 33)).getMax()).isEqualTo(9);
        assertThat(mod10Rule.getPoints(Domino.ofList(21, 11)).getMax()).isEqualTo(8);
        assertThat(mod10Rule.getPoints(Domino.ofList(42, 34)).isMod10()).isTrue();
        assertThat(mod10Rule.getPoints(Domino.ofList(42, 34)).getMax()).isEqualTo(10);
    }

    //Tau Ngau
    @Test
    public void testTauNgau() {
        final Mod10Rule mod10Rule = Mod10Rule.tauNgau();
        assertThat(mod10Rule.getPoints(Domino.ofList(42, 33)).getMax()).isEqualTo(9);
        assertThat(mod10Rule.getPoints(Domino.ofList(42, 34)).isMod10()).isTrue();
        assertThat(mod10Rule.getPoints(Domino.ofList(42, 34)).getMax()).isEqualTo(3);
    }
}