package io.navpil.github.zenzen;

import java.util.Arrays;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class Main8Test {

    public static void main(String[] args) throws ExecutionException, InterruptedException, TimeoutException {
        final ExecutorService executorService = Executors.newFixedThreadPool(2);

        final Integer integer = executorService.invokeAny(Arrays.asList(new Task(500), new Task(200)), 30, TimeUnit.SECONDS);
        System.out.println(integer);
        executorService.shutdown();

    }

    private static class Task implements Callable<Integer> {

        private final int count;

        private Task(int count) {
            this.count = count;
        }

        @Override
        public Integer call() {
            for (int i = 0; i < count; i++) {
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    System.out.println("Interrupted " + count);
                    return -count;
                }
            }
            System.out.println("Finished " + count);
            return count;
        }
    }

}