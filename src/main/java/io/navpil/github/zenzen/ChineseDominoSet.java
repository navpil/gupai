package io.navpil.github.zenzen;

import io.navpil.github.zenzen.dominos.IDomino;
import io.navpil.github.zenzen.dominos.NoDomino;
import io.navpil.github.zenzen.dominos.Domino;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ChineseDominoSet {

    public static List<Domino> dingNiuSet() {
        final List<Domino> dominos = create();
        dominos.remove(Domino.of(5, 4));
        dominos.remove(Domino.of(5, 3));

        dominos.remove(Domino.of(5, 2));
        dominos.remove(Domino.of(4, 3));
        dominos.remove(Domino.of(4, 1));
        dominos.remove(Domino.of(3, 2));
        dominos.remove(Domino.of(4, 2));
        dominos.remove(Domino.of(2, 1));
        return dominos;
    }

    public static List<Domino> create() {

        final ArrayList<Domino> result = new ArrayList<Domino>();
        for (int top = 1; top <= 6; top++) {
            for (int bottom = top; bottom <= 6; bottom++) {
                final Domino domino = new Domino(top, bottom);
                result.add(domino);
                if (domino.isCivil()) {
                    result.add(domino);
                }
            }
        }
        return result;
    }

    public static List<Domino> create(int count) {
        final ArrayList<Domino> result = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            result.addAll(ChineseDominoSet.create());
        }
        return result;
    }

    public static List<Triplet> random8Triplets() {
        List<IDomino> dominoList = new ArrayList<>(ChineseDominoSet.create());
        final NoDomino noDomino = new NoDomino();

        Collections.shuffle(dominoList);

        List<IDomino> triplet = new ArrayList<>();
        List<Triplet> triplets = new ArrayList<>();
        int counter = 0;
        for (IDomino domino : dominoList) {
            triplet.add(domino);
            if (counter < 4 && triplet.size() == 2) {
                counter++;
                triplet.add(noDomino);
                triplets.add(new Triplet(triplet));
                triplet = new ArrayList<>();
            } else if (triplet.size() == 3) {
                triplets.add(new Triplet(triplet));
                triplet = new ArrayList<>();
            }
        }

        return triplets;
    }

    public static List<Triplet> random10Triplets() {
        List<IDomino> dominoList = new ArrayList<>(ChineseDominoSet.create());
        dominoList.add(new NoDomino());

        Collections.shuffle(dominoList);

        List<IDomino> triplet = new ArrayList<>();
        List<Triplet> triplets = new ArrayList<>();
        for (IDomino domino : dominoList) {
            triplet.add(domino);
            if (triplet.size() == 3) {
                triplets.add(new Triplet(triplet));
                triplet = new ArrayList<>();
            }
        }

        return triplets;
    }

}
