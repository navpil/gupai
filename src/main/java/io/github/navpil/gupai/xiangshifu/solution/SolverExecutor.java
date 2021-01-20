package io.github.navpil.gupai.xiangshifu.solution;

import io.github.navpil.gupai.xiangshifu.Triplet;

import java.util.List;
import java.util.concurrent.Callable;

/**
 * Helper class to move solver into a concurrent environment
 */
public class SolverExecutor implements Callable<Solution> {

    private final Solver solver;
    private final List<Triplet> triplets;

    SolverExecutor(Solver solver, List<Triplet> triplets) {
        this.solver = solver;
        this.triplets = triplets;
    }

    @Override
    public Solution call() throws Exception {
        return solver.solve(triplets);
    }
}
