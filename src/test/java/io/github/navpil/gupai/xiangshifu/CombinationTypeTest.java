package io.github.navpil.gupai.xiangshifu;

import io.github.navpil.gupai.CombinationType;
import org.assertj.core.api.Assertions;
import org.junit.Test;

public class CombinationTypeTest {

    @Test
    public void isPair() {
        Assertions.assertThat(CombinationType.HEAVEN.isPair()).isTrue();
    }
}
