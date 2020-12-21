package io.navpil.github.zenzen.jielong.player;

import io.navpil.github.zenzen.dominos.Domino;
import io.navpil.github.zenzen.jielong.Move;
import io.navpil.github.zenzen.jielong.SuanZhangAwareness;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

public class CombiningStrategyPlayerImpl extends AbstractPlayerImpl {

    int reverse = 1;
    boolean doublesFirst = true;

    public CombiningStrategyPlayerImpl(String name, List<Domino> dominos) {
        super(name, dominos);
    }

    public CombiningStrategyPlayerImpl(String name, List<Domino> dominos, SuanZhangAwareness suanZhangAwareness) {
        super(name, dominos, suanZhangAwareness);
    }

    public CombiningStrategyPlayerImpl(String name, List<Domino> dominos, SuanZhangAwareness suanZhangAwareness, boolean doublesFirst) {
        super(name, dominos, suanZhangAwareness);
        this.doublesFirst = doublesFirst;
    }

    @Override
    protected void prioritizePutDown(ArrayList<Domino> dominoes) {
        final List<Domino> byCommon = new ArrayList<>(new HashSet<>(dominoes));
        byCommon.sort(getCommonFirst());
        final List<Domino> bySize = new ArrayList<>(new HashSet<>(dominoes));
        bySize.sort(MinMaxDominoSorter.SMALL_FIRST);

        final Map<Domino, Integer> reverseC = reverseMapping(byCommon);
        final Map<Domino, Integer> reverseS = reverseMapping(bySize);

        double coeff1 = 1.0;
        double coeff2 = 1.0;

        dominoes.sort((d1, d2) -> reverse * (int) ((coeff1 * reverseC.get(d1) + coeff2 * reverseS.get(d1)) - (coeff1 * reverseC.get(d2) + coeff2 * reverseS.get(d2))));
    }

    private Map<Domino, Integer> reverseMapping(List<Domino> byCommon) {
        int counter = 1;
        final HashMap<Domino, Integer> m = new HashMap<>();
        for (Domino domino : byCommon) {
            m.put(domino, counter++);
        }
        return m;
    }

    @Override
    protected void prioritizeLead(ArrayList<Domino> dominoes) {
        final List<Domino> byCommon = new ArrayList<>(new HashSet<>(dominoes));
        byCommon.sort(getCommonFirst());
        final List<Domino> bySize = new ArrayList<>(new HashSet<>(dominoes));
        bySize.sort(MinMaxDominoSorter.BIG_FIRST);

        final Map<Domino, Integer> reverseC = reverseMapping(byCommon);
        final Map<Domino, Integer> reverseS = reverseMapping(bySize);

        double coeff1 = 1.0;
        double coeff2 = 1.0;

        dominoes.sort((d1, d2) ->
        {
            if (doublesFirst) {
                if (isDouble(d1) && !isDouble(d2)) {
                    return -1;
                }
                if (!isDouble(d1) && isDouble(d2)) {
                    return 1;
                }
            }

            return reverse * (int) ((coeff1 * reverseC.get(d1) + coeff2 * reverseS.get(d1)) - (coeff1 * reverseC.get(d2) + coeff2 * reverseS.get(d2)));
        });
    }

    @Override
    protected void prioritizeMove(List<Move> moves) {
        final List<Domino> byCommon = new ArrayList<>(new HashSet<>(getDominos()));
        byCommon.sort(getCommonFirst());
        final List<Domino> bySize = new ArrayList<>(new HashSet<>(getDominos()));
        bySize.sort(MinMaxDominoSorter.BIG_FIRST);

        final Map<Domino, Integer> reverseC = reverseMapping(byCommon);
        final Map<Domino, Integer> reverseS = reverseMapping(bySize);

        double coeff1 = 1.0;
        double coeff2 = 1.0;
        moves.sort((m1, m2) -> {
            Domino d1 = m1.getDomino();
            Domino d2 = m2.getDomino();
            if (doublesFirst) {
                if (isDouble(d1) && !isDouble(d2)) {
                    return -1;
                }
                if (!isDouble(d1) && isDouble(d2)) {
                    return 1;
                }
            }

            return reverse * (int) ((coeff1 * reverseC.get(d1) + coeff2 * reverseS.get(d1)) - (coeff1 * reverseC.get(d2) + coeff2 * reverseS.get(d2)));
        });
    }

    private boolean isDouble(Domino d1) {
        return d1.getPips()[0] == d1.getPips()[1];
    }

    private RarityDominoSorter getCommonFirst() {
        return new RarityDominoSorter(getDominos());
    }
}
