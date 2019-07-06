package ch6;

import lombok.NoArgsConstructor;
import org.apache.commons.lang3.RandomUtils;
import org.assertj.core.api.Assertions;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

public class ForkJoinTasks {

    private ConcurrentLinkedDeque<Integer> trees;
    ForkJoinPool forkJoinPool;
    AtomicInteger atomicInteger = new AtomicInteger();

    @Test
    public void recursiveAction() throws InterruptedException {
        trees = new ConcurrentLinkedDeque();
        int tasks = 10 + new Random().nextInt(20);
        IntStream.range(0, tasks).forEach(n -> trees.addFirst(n));
        Assertions.assertThat(trees).isNotEmpty();
        System.out.println("Task: " + tasks);
        forkJoinPool = new ForkJoinPool();
        forkJoinPool.invoke(new CutTreesAction(atomicInteger.incrementAndGet()));
        forkJoinPool.shutdown();
        forkJoinPool.awaitTermination(1 * tasks, TimeUnit.SECONDS);
        Assertions.assertThat(trees).isEmpty();
        IntStream.range(0, tasks).forEach(n -> trees.addFirst(n));
        while (!trees.isEmpty()) {
            System.out.println("Removing task");
            simpleTaskForFork();
        }
        Assertions.assertThat(trees).isEmpty();
    }

    @Test
    public void ForkJoin() {
        System.out.println(new ForkJoinPool().invoke(new DailyRevenueCalculator()));
    }

    class DailyRevenueCalculator extends RecursiveTask<Double> {

        @Override
        protected Double compute() {
            long idClient = RandomUtils.nextLong();
            GetAccountManagmentPercent percentTask = new GetAccountManagmentPercent();
            System.out.println("Daily computing... " + Thread.currentThread().getName());
            new GetAccountManagmentPercent().fork();
            percentTask.fork();
            return new GetIncome(idClient).compute() * percentTask.join();
        }
    }

    static class GetIncome extends RecursiveTask<Double> {

        GetIncome(Long idClient) {

        }

        @Override
        protected Double compute() {
            try {
                System.out.println("Computing Income... " + Thread.currentThread().getName());
                Thread.sleep(2600);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            double income = RandomUtils.nextDouble(1000, 10000);
            System.out.println("Ready Income... " + income);
            return income;
        }
    }

    @NoArgsConstructor
    static class GetAccountManagmentPercent extends RecursiveTask<Double> {

        @Override
        protected Double compute() {
            try {
                System.out.println("Computing Percent... " + Thread.currentThread().getName());
                Thread.sleep(2500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            double percent = RandomUtils.nextDouble(0.01, 0.05) / 30;
            System.out.println("Ready Percent... " + percent);
            return percent;
        }
    }

    @NoArgsConstructor
    class CutTreesAction extends RecursiveAction {

        int id = 1;

        public CutTreesAction(int id) {
            this.id = id;
        }

        @Override
        protected void compute() {
            System.out.println("Working " + id);
            if (simpleTaskForFork()) return;
            List<CutTreesAction> actionList = new ArrayList<>();
            int start = (this.id + 1);
            if(trees.size()  > 0)
                actionList.add(new CutTreesAction(atomicInteger.incrementAndGet()));
            if(trees.size()  > 8)
                actionList.add(new CutTreesAction(atomicInteger.incrementAndGet()));
            if (trees.size() > 10)
                actionList.add(new CutTreesAction(atomicInteger.incrementAndGet()));
            invokeAll(actionList);
            System.out.println("Forking " + id + " Thread" + Thread.currentThread().getName());

        }
    }

    private boolean simpleTaskForFork() {
        if(trees.poll() != null) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
                Thread.currentThread().interrupt();
            }
            return false;
        }
        return true;
    }
}
