package io.navpil.github.zenzen.jielong;

import io.navpil.github.zenzen.dominos.Domino;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class SuanZhangAwarenessImpl2 implements SuanZhangAwareness {

    private final SuanZhang suanZhang;
    private final double ratio;

    public SuanZhangAwarenessImpl2(SuanZhang suanZhang, double ratio) {
        this.suanZhang = suanZhang;
        this.ratio = ratio;
    }

    @Override
    public List<Move> prioritizeMoves(List<Move> moves, Collection<Domino> dominos, Collection<Domino> putDown) {
        final ArrayList<Move> withSuanZhang = new ArrayList<>();
        final ArrayList<Move> noSuanZhang = new ArrayList<>();

        for (Move move : moves) {
            if (suanZhang.willSuanZhang(move)) {
                withSuanZhang.add(move);
            } else {
                noSuanZhang.add(move);
            }
        }

        if (withSuanZhang.isEmpty() || noSuanZhang.isEmpty()) {
            return new ArrayList<>(moves);
        }
        final ArrayList<Move> result = new ArrayList<>();
        if (shouldSuanZhang(dominos, putDown)) {
            result.addAll(withSuanZhang.stream().map(m -> new SuanZhangMove(m, true)).collect(Collectors.toList()));
            result.addAll(noSuanZhang);
        } else {
            result.addAll(noSuanZhang);
            result.addAll(withSuanZhang.stream().map(m -> new SuanZhangMove(m, true)).collect(Collectors.toList()));
        }
        return result;
    }

    public boolean shouldSuanZhang(Collection<Domino> dominos, Collection<Domino> putDown) {
        int total = 0;
        for (Domino domino : dominos) {
            final int[] pips = domino.getPips();
            total += (pips[0] + pips[1]);
        }
        for (Domino domino : putDown) {
            final int[] pips = domino.getPips();
            total += (pips[0] + pips[1]);
        }
        return total * 1.0 / dominos.size() < (ratio + dominos.size() * 0.7);
    }

}
