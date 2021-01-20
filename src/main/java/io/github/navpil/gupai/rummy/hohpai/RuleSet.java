package io.github.navpil.gupai.rummy.hohpai;

import io.github.navpil.gupai.XuanHePuPai;

public class RuleSet {

    private final GiveDiscardType giveDiscardType;
    private final BrokenGameFix fix;
    private final XuanHePuPai.Pairs pairs;

    //Technically it does not matter what goes first - DISCARD and then DRAW or vice versa
    public enum GiveDiscardType {
        DISCARD_FIRST,
        DRAW_FIRST
    }

    public enum BrokenGameFix {

        //Simplest fix - put one tile face up, currently only supported fix
        KEEP_AN_EYE(false, false, false),

        //Eye can be taken only for the winning purposes, otherwise game proceeds in circles as usual - pure invention
        KEEP_AN_EYE_TAKE_FOR_WIN(false, false, false),

        //All of the next ways allow to take a discarded tile, probably will include in a separate Tok game

        /*
            Combination     Any player      Any tile
            enforced
                +               +               +       -   (taking any tile by any player is a bad idea)
                +               +               -       MahJong
                +               -               +       KapShap(strict)
                +               -               -       JjakMatChuGi
                -               +               +       -   (taking any tile by any player is a bad idea)
                -               +               -       -   (any player grabbing a tile with no combination enforced is a bad idea)
                -               -               +       KapShap
                -               -               -       Gin
         */

        //Allow any user pick the last discared tile, but the combination is enforced
        // (SmallMahJong places one more restriction, but that's too much for generic gameflow to handle)
        MAH_JONG(true, true, false),

        //Same as MahJong, but only the next player can pick a tile
        JJAK_MAT_CHU_GI(true, false, false),

        //Any tile can be taken from the discarded tiles by the next player, no other rules enforced
        KAP_SHAP(false, false, true),

        //Any tile can be taken from the discared tiles by the next player, but only to form a combination
        KAP_SHAP_STRICT(true, false, true),

        //Not present in other eastern games - next player can take a tile, but a combination is not required
        GIN(false, false, false);

        private final boolean combinationEnforced;
        private final boolean playerOutOfOrder;
        private final boolean anyDiscardedTile;

        BrokenGameFix(boolean combinationEnforced, boolean playerOutOfOrder, boolean anyDiscardedTile) {
            this.combinationEnforced = combinationEnforced;
            this.playerOutOfOrder = playerOutOfOrder;
            this.anyDiscardedTile = anyDiscardedTile;
        }
    }

    private final boolean useSok;

    public static RuleSet optimal() {
        return new RuleSet(false, GiveDiscardType.DRAW_FIRST, BrokenGameFix.KEEP_AN_EYE, XuanHePuPai.Pairs.KOREAN);
    }

    public static RuleSet withSok() {
        return new RuleSet(true, GiveDiscardType.DRAW_FIRST, BrokenGameFix.KEEP_AN_EYE, XuanHePuPai.Pairs.KOREAN);
    }

    public RuleSet(boolean useSok, GiveDiscardType giveDiscardType, BrokenGameFix fix, XuanHePuPai.Pairs pairs) {
        this.useSok = useSok;
        this.giveDiscardType = giveDiscardType;
        this.fix = fix;
        this.pairs = pairs;
    }

    public boolean useSok() {
        return useSok;
    }

    public GiveDiscardType getGiveDiscardType() {
        return giveDiscardType;
    }

    public BrokenGameFix getFix() {
        return fix;
    }

    public XuanHePuPai.Pairs getPairs() {
        return pairs;
    }
}
