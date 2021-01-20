package io.github.navpil.gupai.jielong.dingniu;

import io.github.navpil.gupai.jielong.Move;
import io.github.navpil.gupai.jielong.dingniu.ClassicSuanZhang;
import org.assertj.core.api.Assertions;
import org.junit.Test;

public class SuanZhangTest {

    @Test
    public void testWillSuanZhang() {
        final ClassicSuanZhang suanZhang = new ClassicSuanZhang();
        final Move sixFour = new Move(1, 6, 4);
        final Move sixFour2 = new Move(2, 6, 4);

        suanZhang.executeMove(sixFour);

        Assertions.assertThat(suanZhang.willSuanZhang(sixFour2)).isTrue();
        Assertions.assertThat(suanZhang.willSuanZhang(new Move(1, 4, 6))).isFalse();
        suanZhang.executeMove(sixFour2);
        Assertions.assertThat(suanZhang.suanZhangType()).isEqualTo(4);


    }

}
