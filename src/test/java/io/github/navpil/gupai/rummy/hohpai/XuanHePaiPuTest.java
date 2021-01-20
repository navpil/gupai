package io.github.navpil.gupai.rummy.hohpai;

import io.github.navpil.gupai.CombinationType;
import io.github.navpil.gupai.Domino;
import io.github.navpil.gupai.Pairs;
import io.github.navpil.gupai.XuanHePaiPu;
import org.junit.Test;

import static org.assertj.core.api.Assertions.*;

public class XuanHePaiPuTest {

    @Test
    public void hoHpai() {
        final XuanHePaiPu xuanHePaiPu = XuanHePaiPu.hoHpai(true);
        assertThat(xuanHePaiPu.evaluate(Domino.ofList(63,62,23))).isEqualTo(CombinationType.ER_SAN_KAO);
        assertThat(xuanHePaiPu.evaluate(Domino.ofList(63,62,61,66,65,64))).isEqualTo(CombinationType.STRAIGHT);

        assertThat(xuanHePaiPu.evaluate(Domino.ofList(21,32,11,51,62,41))).isEqualTo(CombinationType.none);
        
        assertThat(new XuanHePaiPu(
                false,
                false,
                Pairs.NONE,
                false,
                false,
                false,
                Pairs.KOREAN
        ).evaluate(Domino.ofList(63,62))).isEqualTo(CombinationType.MILITARY_KOREAN_PAIR);
    }
}
