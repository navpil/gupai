package io.github.navpil.gupai.rummy;

import io.github.navpil.gupai.rummy.kapshap.ComputerKapShapPlayer;
import io.github.navpil.gupai.rummy.kapshap.Mod10Pair;
import io.github.navpil.gupai.rummy.kapshap.Ngan;
import io.github.navpil.gupai.ChineseDominoSet;
import io.github.navpil.gupai.DominoParser;
import io.github.navpil.gupai.Domino;
import io.github.navpil.gupai.rummy.kapshap.KapShapHand;
import io.github.navpil.gupai.rummy.kapshap.KapShapPlayer;
import io.github.navpil.gupai.rummy.kapshap.KapShapRuleset;
import io.github.navpil.gupai.rummy.kapshap.KapShapTableVisibleInformation;
import org.assertj.core.api.Assertions;
import org.junit.Test;

import java.util.List;

public class ComputerKapShapPlayerTest {

    @Test
    public void hasWon() {
        final List<Domino> deal = List.of(
                Domino.of(6, 5),
                Domino.of(6, 3),
                Domino.of(2, 2),
                Domino.of(2, 2),
                Domino.of(3, 3)
        );
        final KapShapPlayer kapShapPlayer = new ComputerKapShapPlayer("Name1");
        kapShapPlayer.deal(deal);

        kapShapPlayer.give(Domino.of(3,1));

        Assertions.assertThat(kapShapPlayer.hasWon()).isTrue();

        final KapShapHand expectedWinningHand = new KapShapHand(
                new Ngan(Domino.of(2, 2)),
                List.of(
                        new Mod10Pair(
                                Domino.of(6, 5),
                                Domino.of(6, 3)
                        ),
                        new Mod10Pair(
                                Domino.of(3, 3),
                                Domino.of(3, 1)
                        )
                )
        );
        Assertions.assertThat(kapShapPlayer.getWinningHand()).isEqualTo(expectedWinningHand);
    }

    @Test
    public void shouldCorrectlyChooseNgan() {
        final List<Domino> deal = List.of(
                Domino.of(5, 5),
                Domino.of(5, 5),
                Domino.of(6, 4),
                Domino.of(6, 4),
                Domino.of(2, 2)
        );
        final KapShapPlayer kapShapPlayer = new ComputerKapShapPlayer("Name1");
        kapShapPlayer.deal(deal);

        kapShapPlayer.give(Domino.of(2,2));

        Assertions.assertThat(kapShapPlayer.hasWon()).isTrue();

        final KapShapHand expectedWinningHand = new KapShapHand(
                new Ngan(Domino.of(2, 2)),
                List.of(
                        new Mod10Pair(
                                Domino.of(6, 4),
                                Domino.of(6, 4)
                        ),
                        new Mod10Pair(
                                Domino.of(5, 5),
                                Domino.of(5, 5)
                        )
                )
        );
        Assertions.assertThat(kapShapPlayer.getWinningHand()).isEqualTo(expectedWinningHand);
    }

    @Test
    public void shouldCorrectlyChooseDiscard() {
        final List<Domino> deal = List.of(
                Domino.of(5, 5),
                Domino.of(5, 5),
                Domino.of(6, 3),
                Domino.of(6, 5),
                Domino.of(  2, 2)
        );

        //    public static int[] mods = new int[]{4, 2, 4, 2, 4, 2, 4, 4, 4, 2};
        final KapShapPlayer kapShapPlayer = new ComputerKapShapPlayer("Name1");
        kapShapPlayer.showTable(new KapShapTableVisibleInformation(ChineseDominoSet.create()));
        kapShapPlayer.deal(deal);

        kapShapPlayer.give(Domino.of(2,3));

        Assertions.assertThat(kapShapPlayer.getDiscard()).isEqualTo(Domino.of(2,3));
    }

    @Test
    public void chooseBestDiscard() {
        //    public static int[] mods = new int[]{
        //    0 -> 4, 1 -> 2, 2 -> 4, 3 -> 2, 4 -> 4,

        //    5 -> 2, 9 -> 2, 8 -> 4, 7 -> 4, 6 -> 4, };
        final List<Domino> deal = List.of(
                Domino.of(5, 4),//9 - worst
                Domino.of(5, 3),//8 - good
                Domino.of(4, 2),//3 - good
                Domino.of(3, 1),//4 - good
                Domino.of(  4, 4)// 8 - good
        );

        final KapShapPlayer kapShapPlayer = new ComputerKapShapPlayer("Name1");
        kapShapPlayer.showTable(new KapShapTableVisibleInformation(ChineseDominoSet.create()));
        kapShapPlayer.deal(deal);

        kapShapPlayer.give(Domino.of(6,4));

        Assertions.assertThat(kapShapPlayer.getDiscard()).isEqualTo(Domino.of(5,4));
    }

    @Test
    public void chooseBestDiscardWhenAllAreDiscarded() {
        //    public static int[] mods = new int[]{
        //    0 -> 4, 1 -> 2, 2 -> 4, 3 -> 2, 4 -> 4,

        //    5 -> 2, 9 -> 2, 8 -> 4, 7 -> 4, 6 -> 4, };
        final List<Domino> deal = List.of(
                Domino.of(5, 4),//9 - worst
                Domino.of(5, 3),//8 - good
                Domino.of(4, 2),//3 - good
                Domino.of(3, 1),//4 - good
                Domino.of(  4, 4)//8 - good
        );

        final KapShapPlayer kapShapPlayer = new ComputerKapShapPlayer("Name1");
        final KapShapTableVisibleInformation table = new KapShapTableVisibleInformation(ChineseDominoSet.create(), new KapShapRuleset(KapShapRuleset.Offer.LAST, true, 1, 8, false));
        kapShapPlayer.showTable(table);
        //All twos are gone, should now remove 8
        table.add(Domino.of(6, 6));
        table.add(Domino.of(6, 6));
        table.add(Domino.of(1, 1));
        table.add(Domino.of(1, 1));

        kapShapPlayer.deal(deal);

        kapShapPlayer.give(Domino.of(6,4));


        Assertions.assertThat(kapShapPlayer.getDiscard()).isEqualTo(Domino.of(5,3));
    }

    @Test
    public void chooseBestDiscardWhenAllAreOpen() {
        //    public static int[] mods = new int[]{
        //    0 -> 4, 1 -> 2, 2 -> 4, 3 -> 2, 4 -> 4,

        //    5 -> 2, 9 -> 2, 8 -> 4, 7 -> 4, 6 -> 4, };
        final List<Domino> deal = List.of(
                Domino.of(5, 4),//9 - worst
                Domino.of(5, 3),//8 - good
                Domino.of(4, 2),//3 - good
                Domino.of(3, 1),//4 - good
                Domino.of(  4, 4)//8 - good
        );

        final KapShapPlayer kapShapPlayer = new ComputerKapShapPlayer("Name1");
        final KapShapTableVisibleInformation table = new KapShapTableVisibleInformation(ChineseDominoSet.create(), new KapShapRuleset(KapShapRuleset.Offer.ALL, true, 1, 8, false));
        kapShapPlayer.showTable(table);
        //All ones are one the table, so removing nine is not an option anymore
        table.add(Domino.of(6, 5));
        table.add(Domino.of(6, 5));

        table.add(Domino.of(1, 1));
        table.add(Domino.of(3, 3));
        table.add(Domino.of(6, 6));


        kapShapPlayer.deal(deal);

        kapShapPlayer.give(Domino.of(6,4));


        Assertions.assertThat(kapShapPlayer.getDiscard()).isEqualTo(Domino.of(6,4));
    }

    @Test
    public void handConsistingOfOnlyPairsShouldNotFail() {
        final KapShapTableVisibleInformation table = new KapShapTableVisibleInformation(ChineseDominoSet.create());
        final List<Domino> deal = DominoParser.parseList("[1:1], [6:5], [3:1], [5:5], [4:4], [3:3], [5:4]");
        final KapShapPlayer player = new ComputerKapShapPlayer("First");
        player.showTable(table);
        player.deal(deal);
        player.give(Domino.of(6,4));
    }


    @Test
    public void shouldNotTakeSameAsDiscarded() {
        /*
        Player First was dealt [[6:2], [6:1], [6:5], [6:6], [6:3], [6:5], [3:3]]
Player Second was dealt [[1:1], [6:4], [5:5], [3:1], [5:1], [1:1], [2:2]]
Player Third was dealt [[3:1], [3:2], [5:5], [4:2], [6:4], [3:3], [5:2]]
		--- 21(1) ---
Open dominoes are: []
First was given [2:1]
First discarded [6:3]
		--- 22(2) ---
Open dominoes are: [[6:3]]
Second was given [2:2]
Second discarded [1:1]
		--- 23(3) ---
Open dominoes are: [[6:3], [1:1]]
Third chose to take [1:1]
Third discarded [3:2]
Third was given [4:1]
Third discarded [4:1]
		--- 24(4) ---
Open dominoes are: [[6:3], [3:2], [4:1]]
First chose to take [3:2]
First discarded [3:2]
First was given [6:1]
First discarded [6:1]
         */

        final List<Domino> firstDeal = DominoParser.parseList("[6:2], [6:1], [6:5], [6:6], [6:3], [6:5], [3:3]");
        final KapShapPlayer first = new ComputerKapShapPlayer("First");
        final KapShapTableVisibleInformation table = new KapShapTableVisibleInformation(ChineseDominoSet.create(), new KapShapRuleset(KapShapRuleset.Offer.ALL, true, 1, 8, false));
        first.showTable(table);
        first.deal(firstDeal);

        first.give(Domino.of(2, 1));
        final Domino discard = first.getDiscard();
        table.add(discard);
        table.add(Domino.of(1,1));//Second

        table.remove(Domino.of(1, 1));//Third took it
        table.add(Domino.of(3, 2));
        table.add(Domino.of(4, 1));

        final Domino wished = first.offer(table.getOpenDominoes());
        Assertions.assertThat(wished).isNull();
    }

    @Test
    public void shouldNotFailOnOptionalGet() {
        /*
        Player First was dealt [[5:3], [5:4], [4:4], [1:1], [6:6], [3:2], [4:1]]
        Player Third was dealt [[4:3], [5:2], [2:2], [5:5], [6:6], [5:5], [6:1]]
        --- 14(1) ---
                First was given [6:5]
        First discarded [3:2]
        --- 15(2) ---
                Last discarded domino is: [3:2]
        Third was given [1:1]
        Third discarded [4:3]
        --- 16(3) ---
                Last discarded domino is: [4:3]
        First chose to take [4:3]
        First discarded [4:1]
        First was given [4:2]
        First discarded [4:3]
        --- 17(4) ---
                Last discarded domino is: [4:3]
        Third was given [3:1]
        Third discarded [5:2]
        --- 18(5) ---
                Last discarded domino is: [5:2]
        Exception in thread "main" java.util.NoSuchElementException: No value present
        at java.base/java.util.Optional.get(Optional.java:141)
        at io.navpil.github.zenzen.rummy.kapshap.KapShapPlayer.offer(KapShapPlayer.java:52)
        at io.navpil.github.zenzen.rummy.kapshap.KapShap.runSimulation(KapShap.java:48)
        at io.navpil.github.zenzen.rummy.kapshap.KapShap.main(KapShap.java:18)
         */
        final List<Domino> deal = DominoParser.parseList("[5:3], [5:4], [4:4], [1:1], [6:6], [3:2], [4:1]");
        final KapShapTableVisibleInformation table = new KapShapTableVisibleInformation(ChineseDominoSet.create(), new KapShapRuleset(KapShapRuleset.Offer.LAST, true, 1, 8, false));

        final KapShapPlayer first = new ComputerKapShapPlayer("First");
        first.deal(deal);
        first.showTable(table);
        //First move
        first.give(Domino.of(6,5));
        table.add(Domino.of(3,2));

        table.add(Domino.of(4,3));
        //Second move
        first.give(Domino.of(4,3));
        table.add(Domino.of(4,1));

        first.give(Domino.of(4,2));
        table.add(Domino.of(4,3));
        //Third will discard
        table.add(Domino.of(5,2));

        first.offer(List.of(Domino.of(5,2)));



    }

}
