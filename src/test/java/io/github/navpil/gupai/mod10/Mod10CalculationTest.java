package io.github.navpil.gupai.mod10;

import io.github.navpil.gupai.dominos.Domino;
import org.assertj.core.api.Assertions;
import org.junit.Test;

public class Mod10CalculationTest {

    @Test
    public void mod10() {
        Assertions.assertThat(Mod10Calculation.mod10(Domino.of(5,4), Domino.of(3, 2))).isEqualTo(4);
    }
}