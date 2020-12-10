package io.navpil.github.zenzen.jielong;

public class SuanZhang {

    private int fours = 2;
    private int fives = 4;
    private int ones = 6;
    private int threeTwos = 4;

    private Type type = Type.NONE;

    private int side1 = -1;
    private int side2 = -1;

    public boolean willSuanZhang(Move move) {
        if (side1 == -1) {
            return false;
        }
        if (move.getOutwards() == move.getInwards()) {
            return false;
        }
        switch (move.getSide()) {
            case 1:
                return checkForSuanZhang(move.getOutwards(), this.side2, 1) != Type.NONE;
            case 2:
                return checkForSuanZhang(move.getOutwards(), this.side1, 1) != Type.NONE;
        }
        return false;

    }

    public Type executeMove(Move move) {
        if (side1 == -1) {
            side1 = move.getOutwards();
            side2 = move.getInwards();
        } else {
            switch (move.getSide()) {
                case 1:
                    side1 = move.getOutwards();
                    break;
                case 2:
                    side2 = move.getOutwards();
                    break;
            }
        }
        diminish(move);
        type = checkForSuanZhang(side1, side2, 0);
        return type;
    }

    private void diminish(Move move) {
        if (move.getInwards() != move.getOutwards()) {
            diminish(move.getInwards());
            diminish(move.getOutwards());
        }
    }

    private Type checkForSuanZhang(int side1, int side2, int expected) {
        if (side1 == side2) {
            switch (side1) {
                case 1:
                    if (ones == expected) return Type.CLASSIC;
                    break;
                case 4:
                    if (fours == expected) return Type.CLASSIC;
                    break;
                case 5:
                    if (fives == expected) return Type.CLASSIC;
                    break;
            }
        } else if (side1 == 2 && side2 == 3 || side1 == 3 && side2 == 2) {
            if (threeTwos == expected) {
                return Type.CLASSIC;
            }
        }
        return Type.NONE;
    }

    private void diminish(int points) {
        switch (points) {
            case 1:
                ones--;
                break;
            case 2:
            case 3:
                threeTwos--;
                break;
            case 4:
                fours--;
                break;
            case 5:
                fives--;
        }
    }


    public Type isSuanZhang() {
        return type;
    }

    public enum Type {
        NONE, CLASSIC
    }
}
