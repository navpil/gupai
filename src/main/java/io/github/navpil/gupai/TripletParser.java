package io.github.navpil.gupai;

import io.github.navpil.gupai.dominos.IDomino;

import java.util.ArrayList;
import java.util.List;

/**
 * Parses triplets from a string
 */
public class TripletParser {

    public static List<Triplet> parse(String s) {
        int startIndex = "[Triplet{[".length();
        int endIndex = "[Triplet{[[1:5], [5:5], [2:2]".length();
        int step = "[[1:5], [5:5], [2:2]]}, Triplet{".length();

        final ArrayList<Triplet> triplets = new ArrayList<>();
        for (int i = 0; i < 11; i++) {
            final String substring = s.substring(startIndex + i * step, endIndex + i * step);
            System.out.println(substring);
            triplets.add(parseSingle(substring));
        }

        return triplets;

    }


    public static Triplet parseSingle(String string) {
        final ArrayList<IDomino> dominos = new ArrayList<>();
        dominos.add(DominoParser.parse(string.substring(0, 5)));
        dominos.add(DominoParser.parse(string.substring(7, 12)));
        dominos.add(DominoParser.parse(string.substring(14, 19)));
        return new Triplet(dominos);
    }

}
