package io.github.navpil.gupai.xiangshifu;

import org.assertj.core.api.Assertions;
import org.junit.Test;

public class CombinationTest {

    @Test
    public void isPair() {
        Assertions.assertThat(Combination.HEAVEN.isPair()).isTrue();
    }
}
