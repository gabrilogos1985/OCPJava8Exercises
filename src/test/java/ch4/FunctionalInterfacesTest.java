package ch4;

import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Test;

import java.io.UncheckedIOException;
import java.util.function.*;

public class FunctionalInterfacesTest {
    //TABLE 4.1

    @Test
    public void basicFunctionals() {
        Consumer<Double> consumer = System.out::println;
        consumer.accept(5D);
        Supplier<StringBuilder> supplier = StringBuilder::new;
        Assert.assertThat(supplier.get().toString(), CoreMatchers.equalTo(""));
        Predicate<String> predicate = Boolean::new;
        Assert.assertThat(predicate.test("true"), CoreMatchers.equalTo(true));
        Function<Long, String> function = x -> x.toString();
        Assert.assertThat(function.apply(3434L), CoreMatchers.equalTo("3434"));
        UnaryOperator<RuntimeException> unaryOperator = RuntimeException::new;
        Assert.assertThat(unaryOperator.apply(new IndexOutOfBoundsException("ok")).getCause().getMessage(),
                CoreMatchers.equalTo("ok"));
        BinaryOperator<Long> binaryOperator = (l, l1) -> l + l1;
        Assert.assertThat(binaryOperator.apply(23L, 45L), CoreMatchers.equalTo(68L));
    }

    @Test
    public void biFuntionalInterfaces() {
        BiConsumer<String, StringBuffer> biConsumer = String::contentEquals;
        biConsumer.accept("34", new StringBuffer("34"));
        BiPredicate<Number, Long> biPredicate = Number::equals;
        Assert.assertThat(biPredicate.test(33, 33L), CoreMatchers.equalTo(true));
        BiFunction<String, Long, StringBuilder> biFunction = (st, lon) -> new StringBuilder(st).append(lon);
        Assert.assertThat(biFunction.apply("hi", 2343L).toString(), CoreMatchers.equalTo("hi2343"));
    }

    //TODO: TABLE 4.10


}
