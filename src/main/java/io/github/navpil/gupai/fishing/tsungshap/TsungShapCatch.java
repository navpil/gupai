package io.github.navpil.gupai.fishing.tsungshap;

import io.github.navpil.gupai.dominos.Domino;

import java.util.Collection;
import java.util.Objects;

public class TsungShapCatch {

    private final Collection<Domino> fish;
    private final boolean isSweep;

    public TsungShapCatch(Collection<Domino> fish) {
        this(fish, false);
    }

    private TsungShapCatch(Collection<Domino> fish, boolean isSweep) {
        this.fish = fish;
        this.isSweep = isSweep;
    }

    public TsungShapCatch asSweep() {
        return new TsungShapCatch(fish, true);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TsungShapCatch that = (TsungShapCatch) o;
        return isSweep == that.isSweep &&
                Objects.equals(fish, that.fish);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fish, isSweep);
    }

    @Override
    public String toString() {
        return "TsungShapCatch{" +
                "fish=" + fish +
                ", isSweep=" + isSweep +
                '}';
    }

    public Collection<Domino> getFish() {
        return fish;
    }

    public boolean isSweep() {
        return isSweep;
    }
}
