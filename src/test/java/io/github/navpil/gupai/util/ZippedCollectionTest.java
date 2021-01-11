package io.github.navpil.gupai.util;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

public class ZippedCollectionTest {

    @Test
    public void testZipping() {
        final ZippedCollection<String> zipped = new ZippedCollection<>(
                List.of(
                        List.of("A", "B", "C"),
                        List.of("X", "Y", "Z"),
                        List.of("1", "2", "3")
                ));

        final ArrayList<String> strings = new ArrayList<>(zipped);
        assertThat(strings).isEqualTo(List.of("A", "X", "1", "B", "Y", "2", "C", "Z", "3"));
    }

}