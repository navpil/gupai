package io.github.navpil.gupai.xiangshifu.solution;

import io.github.navpil.gupai.xiangshifu.Triplet;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Solves two Eight pairs in 2 threads in parallel - one for military pairs , one for civil pairs,
 * whichever ends first
 */
public class ParallelEightPairSolver {

    public Solution solve(List<Triplet> triplets) {
        final List<Triplet> triplets2 = new ArrayList<>();
        for (Triplet triplet : triplets) {
            triplets2.add(new Triplet(triplet));
        }

        final ExecutorService executorService = Executors.newFixedThreadPool(2);

        final Solution solution;
        try {
            solution = executorService.invokeAny(Arrays.asList(
                    new SolverExecutor(new Solver(2, EightTripletsWinningCondition.createMilitary()), triplets),
                    new SolverExecutor(new Solver(2, EightTripletsWinningCondition.createCivil()), triplets2)
            ), 600, TimeUnit.SECONDS);
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            System.out.println("Could not find a solution in 5 minutes");
            return new Solution(null, null, triplets, -1);
        }
        executorService.shutdown();
        return solution;
    }

}
