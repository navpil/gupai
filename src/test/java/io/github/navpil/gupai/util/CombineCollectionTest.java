package io.github.navpil.gupai.util;

import org.assertj.core.api.Assertions;
import org.junit.Test;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CombineCollectionTest {

    @Test
    public void testCombinedCollection() {
        final CombineCollection<String> strings = new CombineCollection<>(
                List.of(
                        List.of(
                                "One",
                                "Two"
                        ),
                        Collections.emptySet(),
                        Set.of("Three", "Four"),
                        Collections.emptySet()
                )
        );

        Assertions.assertThat(strings.size()).isEqualTo(4);

        final HashSet<String> strings1 = new HashSet<>(strings);

        Assertions.assertThat(strings1).containsExactlyInAnyOrder("One", "Two", "Three", "Four");


    }

}