package io.navpil.github.zenzen.jielong;

public class SuanZhang {

    private Type type = Type.NONE;

    private GraphSuanZhang graphSuanZhang = new GraphSuanZhang();
    private ClassicSuanZhang classicSuanZhang = new ClassicSuanZhang();

    public void reset() {
        classicSuanZhang.reset();
        graphSuanZhang.reset();
        type = Type.NONE;
    }

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

    public enum Type {
        NONE, SMOTHERED, CLASSIC
    }
}
