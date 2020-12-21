package io.navpil.github.zenzen.jielong;

public class ClassicSuanZhang {

    private int type = 0;

    private int side1 = -1;
    private int side2 = -1;

    private PipTracker pipTracker = new PipTracker();

    public void reset() {
        pipTracker.reset();
        type = 0;
        side1 = -1;
        side2 = -1;

    }

    public boolean willSuanZhang(Move move) {
        if (side1 == -1) {
            return false;
        }
        if (move.getOutwards() == move.getInwards()) {
            return false;
        }
        if (move.getSide() < 0) {
            return false;
        }
        int otherSide;
        if (move.getSide() == 1) {
            otherSide = side2;
        } else {
            otherSide = side1;
        }
        return checkForSuanZhang(move.getOutwards(), otherSide, 1) != 0;

    }

    public int executeMove(Move move) {
        if (move.getSide() < 0) {
            return 0;
        }
        if (side1 == -1) {
            side1 = move.getOutwards();
            side2 = move.getInwards();
        }
        if (move.getInwards() == move.getOutwards()) {
            return 0;
        }
        switch (move.getSide()) {
            case 1:
                side1 = move.getOutwards();
                break;
            case 2:
                side2 = move.getOutwards();
                break;
        }
        pipTracker.diminish(move);
        type = checkForSuanZhang(side1, side2, 0);
        return this.type;
    }

    private int checkForSuanZhang(int side1, int side2, int expected) {
        if (side1 == side2) {
            if (side1 == 1 || side1 == 4 || side1 == 5) {
                if (pipTracker.count(side1) == expected) {
                    return side1;
                }
            }
        } else if (side1 == 2 && side2 == 3 || side1 == 3 && side2 == 2) {
            if ((pipTracker.count(2) + pipTracker.count(3)) == expected) {
                return 2;
            }
        }
        return 0;
    }

    public int suanZhangType() {
        return type;
    }

}
