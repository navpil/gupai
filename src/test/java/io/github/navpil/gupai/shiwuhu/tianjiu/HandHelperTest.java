package io.github.navpil.gupai.shiwuhu.tianjiu;

import io.github.navpil.gupai.dominos.Domino;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.*;

public class HandHelperTest {

    public static final List<Domino> TIAN_JIU = Domino.ofList(66, 66, 63, 54);

    @Test
    public void calculateCombinationsForClassicTianJiu() {
        assertThat(getHelperForRules(RuleSet.TrickType.TIAN_JIU).calculateCombinations(TIAN_JIU).size())
                .describedAs("Fully fledged TianJiu")
                .isEqualTo(1);
    }

    @Test
    public void combinationsForNonMixedPairs() {
        final List<Collection<Domino>> collections = getHelperForRules(RuleSet.TrickType.TIAN_JIU_NO_MIXING).calculateCombinations(TIAN_JIU);
        System.out.println(collections);
        assertThat(collections.size())
                .describedAs("Combinations for non-mixed pairs size")
                .isEqualTo(2);

    }

    @Test
    public void combinationsForMixedPairs() {
        final List<Collection<Domino>> collections = getHelperForRules(RuleSet.TrickType.TIAN_JIU_ONLY_PAIRS).calculateCombinations(TIAN_JIU);
        System.out.println(collections);
        assertThat(collections.stream().map(ArrayList::new).peek(Collections::sort).collect(Collectors.toSet()).size())
                .describedAs("Combinations for mixed pairs size")
                .isEqualTo(4);
    }

    private HandHelper getHelperForRules(RuleSet.TrickType type) {
        return new HandHelper(new RuleSet(type, false, false, false, false));
    }
}