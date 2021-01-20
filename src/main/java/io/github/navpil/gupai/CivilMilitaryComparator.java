package io.github.navpil.gupai;

import java.util.Comparator;

public enum CivilMilitaryComparator implements Comparator<Domino> {
    INSTANCE;

    @Override
    public int compare(Domino d1, Domino d2) {
        if (d1.isCivil() && d2.isMilitary()) {
            return -1;
        } else if (d2.isCivil() && d1.isMilitary()) {
            return 1;
        }
        return 0;
    }
}
