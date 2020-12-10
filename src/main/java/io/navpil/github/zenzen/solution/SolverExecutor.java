package io.navpil.github.zenzen.solution;

import io.navpil.github.zenzen.Triplet;

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
