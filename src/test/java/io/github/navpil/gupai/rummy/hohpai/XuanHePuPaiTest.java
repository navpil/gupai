package io.github.navpil.gupai.rummy.hohpai;

import io.github.navpil.gupai.dominos.Domino;
import org.junit.Test;

import static org.assertj.core.api.Assertions.*;

public class XuanHePuPaiTest {

    @Test
    public void hoHpai() {
        final XuanHePuPai xuanHePuPai = XuanHePuPai.hoHpai();
        assertThat(xuanHePuPai.evaluate(Domino.ofList(63,62,23))).isEqualTo(XuanHePuPai.Combination.ER_SAN_KAO);
        assertThat(xuanHePuPai.evaluate(Domino.ofList(63,62,61,66,65,64))).isEqualTo(XuanHePuPai.Combination.STRAIGHT);

        assertThat(xuanHePuPai.evaluate(Domino.ofList(21,32,11,51,62,41))).isEqualTo(XuanHePuPai.Combination.none);
        
        assertThat(xuanHePuPai.evaluate(Domino.ofList(63,62))).isEqualTo(XuanHePuPai.Combination.MILITARY_KOREAN_PAIR);
    }
}