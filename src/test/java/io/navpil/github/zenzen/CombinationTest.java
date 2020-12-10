package io.navpil.github.zenzen;

import org.assertj.core.api.Assertions;
import org.junit.Test;

import static org.junit.Assert.*;

public class CombinationTest {

    @Test
    public void isPair() {
        Assertions.assertThat(Combination.HEAVEN.isPair()).isTrue();
    }
}