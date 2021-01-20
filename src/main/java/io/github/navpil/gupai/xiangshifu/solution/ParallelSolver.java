package io.github.navpil.gupai.xiangshifu.solution;

import io.github.navpil.gupai.xiangshifu.Triplet;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Parallel solver will take triplets and try to solve them in several threads in parallel.
 *
 * Each thread will use a different setting for a maxNoValueAdded.
 */
public class ParallelSolver {

    private final int threads;
    private final int timeoutInSeconds;

    public ParallelSolver(int threads, int timeoutInSeconds) {
        this.threads = threads;
        this.timeoutInSeconds = timeoutInSeconds;
    }

    public Solution solve(List<Triplet> triplets, WinningCondition winningCondition)  {
        final ArrayList<SolverExecutor> solvers = new ArrayList<>();
        for (int i = 0; i < threads; i++) {
            final List<Triplet> tripletsCopy = copyTriplets(triplets);
            if (winningCondition != null) {
                solvers.add(new SolverExecutor(new Solver(i, winningCondition), tripletsCopy));
            } else {
                solvers.add(new SolverExecutor(new Solver(i), tripletsCopy));
            }
        }
        final ExecutorService executorService = Executors.newFixedThreadPool(threads);

        Solution solution;
        final ExecutorService singleThreaded = Executors.newSingleThreadExecutor();
        try {
            solution = singleThreaded.invokeAny(List.of(new ParallelSolverCallable(executorService, solvers)), timeoutInSeconds, TimeUnit.SECONDS);
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            System.out.println("Could not find a solution in " + (timeoutInSeconds / 60) + " minutes");
            solution = new Solution(null, null, triplets, -1);
        }
        singleThreaded.shutdown();
        executorService.shutdown();
        return solution;
    }


    private List<Triplet> copyTriplets(List<Triplet> triplets) {
        final List<Triplet> triplets2 = new ArrayList<>();
        for (Triplet triplet : triplets) {
            triplets2.add(new Triplet(triplet));
        }
        return triplets2;
    }

}
