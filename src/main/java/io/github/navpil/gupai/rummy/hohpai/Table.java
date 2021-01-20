package io.github.navpil.gupai.rummy.hohpai;

import io.github.navpil.gupai.Domino;

import java.util.Collection;

public interface Table {

    RuleSet getRuleSet();

    Collection<Domino> getAllDeadDominoes();
}
