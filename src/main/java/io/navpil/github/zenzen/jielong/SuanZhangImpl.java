package io.navpil.github.zenzen.jielong;

public class SuanZhangImpl implements SuanZhang {

    private Type type = Type.NONE;

    private final GraphSuanZhang graphSuanZhang = new GraphSuanZhang();
    private final ClassicSuanZhang classicSuanZhang = new ClassicSuanZhang();

    public boolean willSuanZhang(Move move) {
        if (classicSuanZhang.willSuanZhang(move)) {
            return true;
        }
        return graphSuanZhang.willSuanZhang(move) != GraphSuanZhang.Type.NONE;
    }

    public Type executeMove(Move move) {
        final int classicType = classicSuanZhang.executeMove(move);
        final GraphSuanZhang.Type graphType = graphSuanZhang.executeMove(move);
        if (classicType > 0) {
            type = Type.CLASSIC;
        } else if (graphType != GraphSuanZhang.Type.NONE) {
            type = Type.SMOTHERED;
        } else {
            type = Type.NONE;
        }
        return this.type;
    }

    public Type suanZhangType() {
        return type;
    }
}
