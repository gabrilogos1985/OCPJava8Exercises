package ch4;

import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Test;

import java.util.OptionalDouble;
import java.util.OptionalInt;
import java.util.OptionalLong;
import java.util.Random;
import java.util.function.DoubleSupplier;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.LongStream;

public class PrimitiveStreamsTest {

    @Test
    public void averageStreams() {
        IntStream intStream = IntStream.of(3, 4,5);
        OptionalDouble average = intStream.average();
        int suma = intStream.sum();
        double avg = average.getAsDouble();
        DoubleSupplier doubleSupplier = () -> new Random().nextDouble();
        average.orElseGet(doubleSupplier);
        LongStream longStream = LongStream.of(1, 2, 3);
        longStream.average().orElseGet(doubleSupplier);
        long sumLong = longStream.sum();
        DoubleStream doubleStream = DoubleStream.of(1, 2,3);
        doubleStream.average().orElseGet(doubleSupplier);
        double doubleSum = doubleStream.sum();
    }

    @Test
    public void optionals() {
        int intVal = OptionalInt.empty().getAsInt();
        long longVal = OptionalLong.empty().getAsLong();
        double doubleVal = OptionalDouble.empty().getAsDouble();

        Assert.assertThat(intVal + longVal + doubleVal, CoreMatchers.equalTo(0));
    }

    @Test
    public void maxStream() {
        OptionalInt optionalInt = IntStream.of(1, 2,3).max();
        OptionalLong optionalLong = LongStream.of(1, 2,3).max();
        OptionalDouble optionalDouble = DoubleStream.of(1, 2,3).max();
    }

    @Test(timeout = 500)
    public void infinitePi() {
        DoubleStream doubles = DoubleStream.generate(() -> Math.PI);
        OptionalDouble min = doubles.peek(System.out::println).min(); // runs infinitely
    }
}
