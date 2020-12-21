package io.navpil.github.zenzen.jielong;

import io.navpil.github.zenzen.dominos.Domino;

import java.util.Collection;
import java.util.List;

public interface SuanZhangAwareness {

    List<Move> prioritizeMoves(List<Move> moves, Collection<Domino> dominos, Collection<Domino> putDown);

}
