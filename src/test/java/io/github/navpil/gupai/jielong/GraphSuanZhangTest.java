package io.github.navpil.gupai.jielong;

import org.assertj.core.api.Assertions;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class GraphSuanZhangTest {
    //[2:6][6:3][3:1][1:6][6:5][5:6][6:1]
    private List<Move> fours = List.of(
            new Move(1, 2, 6),
            new Move(1, 6, 3),
            new Move(1, 3, 1),
            new Move(1, 1, 6),
            new Move(1, 6, 5),
            new Move(1, 5, 6),
            new Move(1, 6, 1)
    );
    //[6:3][3:1][1:6][6:5][5:6][6:1][1:3]
    private List<Move> fives = List.of(
            new Move(1, 6, 3),
            new Move(1, 3, 1),
            new Move(1, 1, 6),
            new Move(1, 6, 5),
            new Move(1, 5, 6),
            new Move(1, 6, 1),
            new Move(1, 1, 3)
    );

    @Test
    public void testWhySuanZhang() {
        String p = "[3:1], [1:3], [3:3], [3:6], [6:6], [6:5], [5:5], [5:6], [6:6], [6:1], [1:6]";
        List<Move> moves = getMoves(p);

        check(moves, GraphSuanZhang.Type.FIVES);
    }

    private List<Move> getMoves(String p) {
        final String[] m = p.split(", ");
        List<Move> moves = new ArrayList<>();
        for (String move : m) {
            moves.add(new Move(1, Integer.parseInt("" + move.charAt(1)), Integer.parseInt("" + move.charAt(3))));
        }
        return moves;
    }

    @Test
    public void shouldNotSuanZhangOnHiddenFours() {
        String p = "[6:4], [4:4], [4:4], [4:6]";
        List<Move> moves = getMoves(p);

        check(moves, GraphSuanZhang.Type.NONE);
    }

    @Test
    public void testWhySuanZhang2() {
        final List<Move> moves = getMoves("[2:6], [6:1], [1:5], [5:5], [5:6], [6:5], [5:1], [1:1], [1:3], [3:3], [3:6], [6:6], [6:1]");
        check(moves, GraphSuanZhang.Type.FOURS);

    }


    @Test
    public void suanZhangFromVideo() {
        final List<Move> moves = getMoves("[4:4], [4:4], [4:6], [6:6], [6:5], [5:5], [5:5], [5:1], [1:6], [6:6], [6:1], [1:5]");
        final GraphSuanZhang sz = new GraphSuanZhang(true);
        for (int i = 0; i < moves.size() - 1; i++) {
            Assertions.assertThat(sz.executeMove(moves.get(i))).isEqualTo(GraphSuanZhang.Type.NONE);
        }
        Assertions.assertThat(sz.executeMove(moves.get(moves.size() - 1))).isEqualTo(GraphSuanZhang.Type.NONE);
    }

    @Test
    public void executeMove() {
        check(this.fours, GraphSuanZhang.Type.FOURS);
    }

    @Test
    public void executeMoveFivesSuanZhang() {
        check(fives, GraphSuanZhang.Type.FIVES);
    }

    private void check(List<Move> moves, GraphSuanZhang.Type type) {
        final GraphSuanZhang sz = new GraphSuanZhang();
        for (int i = 0; i < moves.size() - 1; i++) {
            Assertions.assertThat(sz.executeMove(moves.get(i))).isEqualTo(GraphSuanZhang.Type.NONE);
        }
        Assertions.assertThat(sz.executeMove(moves.get(moves.size() - 1))).isEqualTo(type);
    }

    @Test
    public void executeMoveFivesSuanZhangReverse() {
        final GraphSuanZhang sz = new GraphSuanZhang();
        for (int i = fives.size() - 1; i >= 2; i--) {
            Assertions.assertThat(sz.executeMove(revert(fives.get(i)))).isEqualTo(GraphSuanZhang.Type.NONE);
        }
        //FIVES SuanZhang happens sooner than on the 0th element
        Assertions.assertThat(sz.executeMove(revert(fives.get(1)))).isEqualTo(GraphSuanZhang.Type.FIVES);
    }
    @Test
    public void executeMoveFoursFivesSuanZhangReverse() {
        final GraphSuanZhang sz = new GraphSuanZhang();
        for (int i = this.fours.size() - 1; i >= 1; i--) {
            Assertions.assertThat(sz.executeMove(revert(this.fours.get(i)))).isEqualTo(GraphSuanZhang.Type.NONE);
        }
        //FIVES SuanZhang happens sooner than on the 0th element
        Assertions.assertThat(sz.executeMove(revert(this.fours.get(0)))).isEqualTo(GraphSuanZhang.Type.FOURS);
    }

    private Move revert(Move m) {
        return new Move(m.getSide(), m.getOutwards(), m.getInwards());
    }
}