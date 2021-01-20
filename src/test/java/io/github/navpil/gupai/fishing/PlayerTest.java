package io.github.navpil.gupai.fishing;

import io.github.navpil.gupai.DominoParser;
import io.github.navpil.gupai.Domino;
import io.github.navpil.gupai.fishing.tiuu.Catch;
import io.github.navpil.gupai.fishing.tiuu.ComputerPlayer;
import io.github.navpil.gupai.fishing.tiuu.Player;
import io.github.navpil.gupai.fishing.tiuu.RuleSet;
import io.github.navpil.gupai.fishing.tiuu.Table;
import org.assertj.core.api.Assertions;
import org.junit.Test;

public class PlayerTest {

    @Test
    public void test() {
        /*
        Table pool: Bag{map={[1:1]=c:2, [3:1]=c:2, [3:3]=c:3, [4:1]=c:1, [4:2]=c:1, [4:3]=c:1, [4:4]=c:1, [5:1]=c:2, [6:1]=c:1, [6:2]=c:1, [6:5]=c:1}}
        Player{name='Comp-1', dominos=Bag{map={[2:1]=c:1, [2:2]=c:1, [3:1]=c:1, [4:3]=c:1, [5:1]=c:1, [5:3]=c:2, [5:5]=c:1}}}
        Comp-1 catches Catch{bait=[5:1], fish=Bag{map={[4:2]=c:1}}}
        Comp-1 catches Catch{bait=[6:5], fish=Bag{map={[6:5]=c:1}}}
         */

        final Table table = new Table(RuleSet.mixed());
        final Player player = new ComputerPlayer("Test");
        player.showTable(table);
        player.deal(DominoParser.parseBag("[2:1]=c:1, [2:2]=c:1, [3:1]=c:1, [4:3]=c:1, [5:1]=c:1, [5:3]=c:2, [5:5]=c:1"));
        table.setupPool(DominoParser.parseBag("[1:1]=c:2, [3:1]=c:2, [3:3]=c:3, [4:1]=c:1, [4:2]=c:1, [4:3]=c:1, [4:4]=c:1, [5:1]=c:2, [6:1]=c:1, [6:2]=c:1, [6:5]=c:1"));

        final Catch fish = player.fish(table.getPool());
        Assertions.assertThat(fish.getBait()).isEqualTo(Domino.of(53));
    }

}
