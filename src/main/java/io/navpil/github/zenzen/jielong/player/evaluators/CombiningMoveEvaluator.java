package io.navpil.github.zenzen.jielong.player.evaluators;

import io.navpil.github.zenzen.jielong.Move;
import io.navpil.github.zenzen.jielong.TableVisibleInformation;
import io.navpil.github.zenzen.jielong.player.MutableInteger;
import io.navpil.github.zenzen.jielong.player.PlayerState;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Combines several evaluators into one, runs all of them and combines result
 */
public class CombiningMoveEvaluator implements MoveEvaluator {

    private final List<Tuple<MoveEvaluator, PriorityNormalizer>> evaluators;

    public CombiningMoveEvaluator() {
        this.evaluators = new ArrayList<>();
    }

    public CombiningMoveEvaluator addEvaluator(MoveEvaluator moveEvaluator, PriorityNormalizer priorityNormalizer) {
        evaluators.add(new Tuple<>(moveEvaluator, priorityNormalizer));
        return this;
    }

    public CombiningMoveEvaluator addEvaluator(MoveEvaluator moveEvaluator, double ratio) {
        evaluators.add(new Tuple<>(moveEvaluator, RatioNormalizer.fromRatio(ratio)));
        return this;
    }

    public CombiningMoveEvaluator addEvaluator(MoveEvaluator moveEvaluator) {
        evaluators.add(new Tuple<>(moveEvaluator, (i) -> i));
        return this;
    }

    @Override
    public List<Integer> evaluateMove(List<Move> moves, TableVisibleInformation tableVisibleInformation, PlayerState playerState) {
        return evaluate(moves, tableVisibleInformation, playerState, MoveEvaluator::evaluateMove);
    }

    @Override
    public List<Integer> evaluatePutDown(List<Move> moves, TableVisibleInformation tableVisibleInformation, PlayerState playerState) {
        return evaluate(moves, tableVisibleInformation, playerState, MoveEvaluator::evaluatePutDown);
    }

    @Override
    public List<Integer> evaluateLead(List<Move> moves, TableVisibleInformation tableVisibleInformation, PlayerState playerState) {
        return evaluate(moves, tableVisibleInformation, playerState, MoveEvaluator::evaluateLead);
    }

    private List<Integer> evaluate(List<Move> moves, TableVisibleInformation tableVisibleInformation, PlayerState playerState, MoveFunction moveFunction) {
        List<MutableInteger> result = new ArrayList<>();
        for (Move move : moves) {
            result.add(new MutableInteger());
        }
        for (Tuple<MoveEvaluator, PriorityNormalizer> evaluator : evaluators) {
            final List<Integer> evaluated = moveFunction.evaluate(evaluator.a, moves, tableVisibleInformation, playerState);
            final List<Integer> normalized = evaluator.b.apply(evaluated);
            for (int i = 0; i < normalized.size(); i++) {
                result.get(i).add(normalized.get(i));
            }
        }
        return result.stream().map(MutableInteger::getCount).collect(Collectors.toList());
    }

    /**
     * Applies some logic to the list of priorities and returns the result.
     */
    @FunctionalInterface
    public interface PriorityNormalizer extends Function<List<Integer>, List<Integer>> {
    }

    @FunctionalInterface
    public interface MoveFunction {
        List<Integer> evaluate(MoveEvaluator evaluator, List<Move> moves, TableVisibleInformation tableVisibleInformation, PlayerState playerState);
    }

    private static class Tuple<A, B> {
        private A a;
        private B b;

        public Tuple(A a, B b) {
            this.a = a;
            this.b = b;
        }
    }
}
