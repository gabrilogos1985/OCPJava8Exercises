package ch6;

import org.apache.commons.lang3.RandomUtils;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import sun.misc.IOUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.concurrent.*;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;

public class ExecutorServiceTest {
    @Test
    public void execute() throws ExecutionException, InterruptedException {
        int counter = 0;
        // DoesNot Compile Executors.newSingleThreadExecutor().execute(() -> counter + 1);
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(() -> System.out.println(counter + 1));
        Future<?> submit = executorService.submit(() -> System.out.println(counter + 10));
        Object getRunnableSubmit = submit.get();
        System.out.println("GET "  + getRunnableSubmit);
        Assertions.assertThat(getRunnableSubmit).isNull();
        String getCallableSubmit = executorService.submit(() -> "este si regresa").get();
        System.out.println("SUB - GET "  + getCallableSubmit);
        Assertions.assertThat(getCallableSubmit).isEqualTo("este si regresa");
        // DOES NOT COMPILE System.out.println("SUB - GET "  + executorService.submit((l) -> "este si regresa").get());
    }

    private transient boolean schuedleAtFixedResult;
    @Test(timeout = 500)
    public void scheduleExecutor() throws InterruptedException {
        Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(
                () -> System.out.println("All ok " + (schuedleAtFixedResult = RandomUtils.nextBoolean())),
                0, 40, TimeUnit.MILLISECONDS);
        schuedleAtFixedResult = true;
        while (schuedleAtFixedResult) {Thread.sleep(20);};

//        Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(
//                () -> "NO COMPILA CON CALLABLE");
    }

    @Test
    public void collectionsConcurrent() {
        List<Integer> l1 = Arrays.asList(1,2,3);
        List<Integer> l2 = new CopyOnWriteArrayList<>(l1);
        Set<Integer> s3 = new ConcurrentSkipListSet<>();
        s3.addAll(l1);

        for(Integer item: l2) l2.add(4); // x1
        for(Integer item: s3) s3.add(5); // x2
        System.out.println(l1.size()+" "+l2.size()+" "+s3.size());
        Assertions.assertThat(l1.size()).isEqualTo(3);
        Assertions.assertThat(l2.size()).isEqualTo(6);
        Assertions.assertThat(s3.size()).isEqualTo(4);
    }

    private transient int times;
    @Test
    public void collectionGrowingInfitely() {
        Set<Integer> cset = new ConcurrentSkipListSet<>();
        cset.add(1);
        cset.forEach(n -> {
            times++;
            cset.add(n + (10 * times));
            System.out.println(cset);
        });
        Assertions.assertThat(cset).hasSize(2);
        for (Integer num : cset) {
            times++;
            System.out.printf("iterando %s of %s\n", num, cset);
            int lastSize = cset.size();
            cset.add(num + (10 * times));
            Assertions.assertThat(cset).hasSize(lastSize + 1);
            if(cset.size() > RandomUtils.nextInt(1000, 50000)) {
                System.out.println("SALIENDO DE EJECUCION INFINITA " + cset.size());
                return;
            }
        }
    }


    @Test(timeout = 5000)
    public void reduceStreamParallel() {
        String s;
        do {
            // INCREASE THIS TO CHEACK PARRALEL IS NOT PREDICTABLE, CHECK THE NEXT TEST
            s = Arrays.asList("duck", "chicken", "flamingo", "pelican", "dos", "tres", "cuatro")
                    .parallelStream().parallel() // q1
                    .reduce("inicio... ",
                            (c1, c2) -> c1 + c2 + "- ", // q2
                            (s1, s2) -> s1 + s2);
            System.out.println(s); // q3
        }while (("inicio... duck- inicio... chicken- inicio... flamingo- inicio... pelican-" +
                    " inicio... dos- inicio... tres- inicio... cuatro- ").equals(s));

    }

    @Test(timeout = 5000)
    public void reduceStreamParallelMany() {
        List<String> stringList = new ArrayList<>();
        StringBuilder expectedConcat = new StringBuilder();
        // INCREASE THIS TO CHEACK PARRALEL IS NOT PREDICTABLE
        IntStream.range(1, 70).forEach(n-> {
            String numStr = n + "- ";
            stringList.add(numStr);
            expectedConcat.append("inicio... ").append(numStr);
        });
        String s;
        do {
            s = stringList
                    .parallelStream().parallel() // q1
                    .reduce("inicio... ",
                            (c1, c2) -> c1 + c2, // q2
                            (s1, s2) -> s1 + s2);
            System.out.println(s); // q3
        }while (expectedConcat.toString().equals(s));
    }

    @Test(timeout = 5000)
    public void neverShutdown() {
        ExecutorService service = Executors.newScheduledThreadPool(10);
        DoubleStream.of(3.14159,2.71828) // b1
                .forEach(c -> service.submit( // b2
                        () -> System.out.println(10*c))); // b3
        service.execute(() -> System.out.println("Printed")); // b4
    }

    public static void main(String ...args) {
        ExecutorService service = Executors.newScheduledThreadPool(10);
        DoubleStream.of(3.14159,2.71828) // b1
                .forEach(c -> service.submit( // b2
                        () -> System.out.println(10*c))); // b3
        service.execute(() -> System.out.println("Printed")); // b4S
    }
}
