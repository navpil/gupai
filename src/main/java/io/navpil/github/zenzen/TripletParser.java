package io.navpil.github.zenzen;

import java.util.ArrayList;
import java.util.List;

public class TripletParser {

    public static List<Triplet> parse(String s) {
        int startIndex = "[Triplet{[".length();
        int endIndex = "[Triplet{[[1:5], [5:5], [2:2]".length();
        int step = "[[1:5], [5:5], [2:2]]}, Triplet{".length();

        final ArrayList<Triplet> triplets = new ArrayList<>();
        for (int i = 0; i < 11; i++) {
            final String substring = s.substring(startIndex + i * step, endIndex + i * step);
            System.out.println(substring);
            triplets.add(Triplet.parse(substring));
        }

        return triplets;

    }

}
