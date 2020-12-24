package io.navpil.github.zenzen.mod10;

import io.navpil.github.zenzen.dominos.Domino;
import org.assertj.core.api.Assertions;
import org.junit.Test;

import static org.junit.Assert.*;

public class Mod10CalculationTest {

    @Test
    public void mod10() {
        Assertions.assertThat(Mod10Calculation.mod10(Domino.of(5,4), Domino.of(3, 2))).isEqualTo(4);
    }
}