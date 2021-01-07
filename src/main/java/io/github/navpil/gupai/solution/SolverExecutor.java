package io.github.navpil.gupai.solution;

import io.github.navpil.gupai.Triplet;

import java.util.List;
import java.util.concurrent.Callable;

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
