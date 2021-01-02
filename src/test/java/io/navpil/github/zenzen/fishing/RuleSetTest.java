package io.navpil.github.zenzen.fishing;

import io.navpil.github.zenzen.DominoParser;
import io.navpil.github.zenzen.dominos.Domino;
import io.navpil.github.zenzen.util.Bag;
import io.navpil.github.zenzen.util.HashBag;
import io.navpil.github.zenzen.util.TreeBag;
import org.assertj.core.api.Assertions;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class RuleSetTest {

    @Test
    public void canCatch() {

        final RuleSet culin = RuleSet.culin();
        Assertions.assertThat(culin.canCatch(Domino.of(3,3), Domino.of(3,3))).isTrue();
    }

    @Test
    public void checkLenientRuleCalculation() {
//        "Comp-1 caught Bag{map={[2:2]=c:1, [3:1]=c:1, [3:2]=c:1, [3:3]=c:3, [4:1]=c:1, [4:3]=c:1, [4:4]=c:1, [5:1]=c:3, [5:5]=c:4, [6:1]=c:1, [6:2]=c:1, [6:4]=c:4, [6:5]=c:2, [6:6]=c:2}} which gives 420 points"
//        "Comp-2 caught Bag{map={[1:1]=c:2, [2:2]=c:3, [3:1]=c:3, [3:3]=c:1, [4:3]=c:1, [4:4]=c:3, [5:1]=c:1, [5:2]=c:1, [5:3]=c:1, [6:1]=c:2}} which gives 70 points"
//        "Comp-3 caught Bag{map={[1:1]=c:2, [2:1]=c:2, [3:2]=c:1, [4:1]=c:1, [4:2]=c:2, [5:2]=c:1, [5:3]=c:1, [5:4]=c:2, [6:1]=c:1, [6:2]=c:1, [6:3]=c:2, [6:5]=c:2, [6:6]=c:2}} which gives 510 points"

        final RuleSet rules = RuleSet.mixed();

        final List<Domino> dominos = DominoParser.parseBag("[1:1]=c:2, [2:2]=c:3, [3:1]=c:3, [3:3]=c:1, [4:3]=c:1, [4:4]=c:3, [5:1]=c:1, [5:2]=c:1, [5:3]=c:1, [6:1]=c:2");
        System.out.println(dominos);
        final Bag<Domino> dd = new TreeBag<>(dominos);
        final int i = rules.calculatePoints(dd);
        Assertions.assertThat(i).isEqualTo(260);
    }
}