package io.navpil.github.zenzen.rummy.hohpai;

import io.navpil.github.zenzen.dominos.Domino;
import org.junit.Test;

import static org.assertj.core.api.Assertions.*;

public class XuanHePuPaiTest {

    @Test
    public void hoHpai() {
        assertThat(XuanHePuPai.hoHpai().evaluate(Domino.ofList(63,62,23))).isEqualTo(XuanHePuPai.Combination.ER_SAN_KAO);
        assertThat(XuanHePuPai.hoHpai().evaluate(Domino.ofList(63,62,61,66,65,64))).isEqualTo(XuanHePuPai.Combination.STRAIGHT);

        assertThat(XuanHePuPai.hoHpai().evaluate(Domino.ofList(21,32,11,51,62,41))).isEqualTo(XuanHePuPai.Combination.none);
    }
}