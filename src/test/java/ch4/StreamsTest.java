package ch4;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public class StreamsTest {
    @Test(timeout = 1000)
    public void infiniteStream() {
        // use awaitability
        Stream<String> infinite = Stream.generate(() -> "chimp");
        infinite.forEach(System.out::println);
    }

    @Test
    public void findFirstONInfinite() {
        Assert.assertTrue(Stream.generate(() -> "chimp").findFirst().isPresent());
    }
    @Test(timeout = 500)
    public void findNoneONInfinite() {
        Assert.assertFalse(Stream.generate(() -> "chimp").noneMatch((el) -> Character.isLetter(el.charAt(0))));
        Stream.generate(() -> "2").noneMatch((el) -> Character.isLetter(el.charAt(0)));
    }

    @Test
    public void badPeek() {
        List<Integer> nums = new ArrayList<>(Arrays.asList(1));
        List<String> letter = new ArrayList<>(Arrays.asList("a"));
        Stream.of(nums, letter).map(List::size).forEach(System.out::println);
        Stream.of(nums, letter).peek(List::clear).map(List::size).forEach(System.out::println);
    }
}
