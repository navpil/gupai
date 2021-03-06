package io.github.navpil.gupai.jielong.player.evaluators;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Changes the priority according to some ratio
 */
public class RatioNormalizer implements CombiningMoveEvaluator.PriorityNormalizer {

    private final double ratio;

    public RatioNormalizer(double ratio) {
        this.ratio = ratio;
    }

    public static RatioNormalizer fromRatio(double ratio) {
        return new RatioNormalizer(ratio);
    }

    @Override
    public List<Integer> apply(List<Integer> integers) {
        return integers.stream().map(i -> (int)(i * ratio)).collect(Collectors.toList());
    }
}
