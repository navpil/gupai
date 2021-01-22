package io.github.navpil.gupai.xiangshifu;

import io.github.navpil.gupai.Domino;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class TripletParserTest {

    @Test
    public void testParsingTwoTriplets() {
        final Triplet triplet = new Triplet(Domino.ofList(22, 61, 33));
        final String s = List.of(triplet, triplet).toString();
        System.out.println(s);

        final List<Triplet> parse = TripletParser.parse(s);
        assertThat(parse.size()).isEqualTo(2);

        assertThat(parse.get(0)).isEqualTo(triplet);
        assertThat(parse.get(1)).isEqualTo(triplet);
    }

    @Test
    public void testParsingSingleTriplet() {
        final Triplet triplet = new Triplet(Domino.ofList(22, 61, 33));
        final String s = List.of(triplet).toString();
        System.out.println(s);

        final List<Triplet> parse = TripletParser.parse(s);
        assertThat(parse.size()).isEqualTo(1);

        assertThat(parse.get(0)).isEqualTo(triplet);
    }

    @Test
    public void testParsing8Triplets() {
        final List<Triplet> parse = TripletParser.parse("[Triplet{[[ - ], [2:2], [6:2]]}, Triplet{[[ - ], [3:3], [5:4]]}, Triplet{[[ - ], [5:1], [6:6]]}, Triplet{[[ - ], [1:1], [6:6]]}, Triplet{[[3:2], [4:3], [6:3]]}, Triplet{[[3:3], [6:1], [6:4]]}, Triplet{[[4:1], [4:4], [6:5]]}, Triplet{[[4:4], [5:5], [6:1]]}, Triplet{[[2:1], [6:4], [6:5]]}, Triplet{[[3:1], [5:2], [5:3]]}, Triplet{[[1:1], [2:2], [5:5]]}, Triplet{[[3:1], [4:2], [5:1]]}]");
        assertThat(parse.size()).isEqualTo(12);
    }

    @Test
    public void testParsingLessThan8Triplets() {
        List<Triplet> listToParse = new ArrayList<>();
        final Triplet triplet = new Triplet(Domino.ofList(22, 61, 33));
        for (int i = 0; i < 15; i++) {
            listToParse.add(triplet);
        }

        final List<Triplet> parse = TripletParser.parse(listToParse.toString());
        assertThat(parse.size()).isEqualTo(15);
    }

}
