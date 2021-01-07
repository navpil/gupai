package io.github.navpil.gupai.solution;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.Future;

public class ParallelSolver2 implements Callable<Solution>{

    private final Executor e;
    private final Collection<? extends Callable<Solution>> solvers;

    public ParallelSolver2(Executor e,
                           Collection<? extends Callable<Solution>> solvers) {
        this.e = e;
        this.solvers = solvers;

    }

    public Solution call()
            throws InterruptedException {
        CompletionService<Solution> ecs
                = new ExecutorCompletionService<>(e);
        int n = solvers.size();
        List<Future<Solution>> futures
                = new ArrayList<>(n);
        Solution result = null;
        try {
            for (Callable<Solution> s : solvers)
                futures.add(ecs.submit(s));
            for (int i = 0; i < n; ++i) {
                try {
                    Solution r = ecs.take().get();
                    if (r.getMoves() != null) {
                        result = r;
                        break;
                    }
                } catch (ExecutionException ignore) {}
            }
        }
        finally {
            for (Future<Solution> f : futures)
                f.cancel(true);
        }

        return result;
    }
}
