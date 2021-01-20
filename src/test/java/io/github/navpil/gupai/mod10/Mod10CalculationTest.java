package io.github.navpil.gupai.mod10;

import io.github.navpil.gupai.Domino;
import org.assertj.core.api.Assertions;
import org.junit.Test;

import java.util.Arrays;

public class Mod10CalculationTest {

    @Test
    public void mod10() {
        Assertions.assertThat((int) Mod10Rule.KOL_YE_SI.getPoints(Arrays.asList(Domino.of(5, 4), Domino.of(3, 2))).getMax()).isEqualTo(4);
    }
}
