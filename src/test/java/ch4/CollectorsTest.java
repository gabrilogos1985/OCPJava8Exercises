package ch4;

import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Test;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CollectorsTest {

    @Test
    public void simpleCollectors() {
        TreeSet<Integer> intTreeSet = Stream.of(1, 2, 3, 4).collect(Collectors.toCollection(TreeSet::new));
        double averageDouble = Stream.of(1, 2, 3, 4).collect(Collectors.averagingDouble(d -> d /2));
        double averageIntDOuble = Stream.of(1, 2, 3, 4).collect(Collectors.averagingInt(d -> d /3));
        double averageLongDouble = Stream.of(1, 2, 3, 4).collect(Collectors.averagingLong(d -> d));
        String simpleJoin = Stream.of(1, 2, 3, 4).map(d -> d + "").collect(Collectors.joining());
        String simplePipeJoin = Stream.of(1, 2, 3, 4).map(d -> d + "").collect(Collectors.joining("|"));
        Long count = Stream.of(1, 2, 3, 4, 1).map(d -> d + "").collect(Collectors.counting());
        Assert.assertEquals(count, Long.valueOf(5));
        Long sumLong = Stream.of(1, 2, 3, 4).map(d -> d + "").collect(Collectors.summingLong(l -> Long.valueOf(l)));
        Assert.assertEquals(sumLong, Long.valueOf(10));
        int sumInt = Stream.of(1, 2, 3, 4).map(d -> d + "").collect(Collectors.summingInt(l -> Integer.valueOf(l)));
        Assert.assertEquals(sumInt, 10);
        Optional<Integer> maxby = Stream.of(1, 2, 3, 4).collect(Collectors.maxBy((Integer v1, Integer v2)-> v2 - v1));
        Assert.assertEquals(maxby.get(), Integer.valueOf(1));
        DoubleSummaryStatistics doubleSummary = Stream.of(1, 2, 3, 4).collect(Collectors.summarizingDouble(d -> d + .1));
        Assert.assertThat(doubleSummary.getAverage(), CoreMatchers.equalTo(2.6));
        Assert.assertThat(doubleSummary.getCount(), CoreMatchers.equalTo(4L));
        Assert.assertThat(doubleSummary.getMax(), CoreMatchers.equalTo(4.1));
        Assert.assertThat(doubleSummary.getMin(), CoreMatchers.equalTo(1.1));
        Assert.assertThat(doubleSummary.getSum(), CoreMatchers.equalTo(2.6*4));
        IntSummaryStatistics intSummary = Stream.of(1, 2, 3, 4).collect(Collectors.summarizingInt(i -> i / 3));
        DoubleSummaryStatistics ddSummary = Stream.of(1, 2, 3, 4).collect(Collectors.summarizingDouble(i -> i / 3));
        Assert.assertThat(intSummary.getSum(), CoreMatchers.equalTo(2L));
        Assert.assertThat(ddSummary.getSum(), CoreMatchers.equalTo(2.0));
        Assert.assertThat(intSummary.getMax(), CoreMatchers.equalTo(4/3));
        Assert.assertThat(intSummary.getMin(), CoreMatchers.equalTo(1/3));
        Assert.assertThat(ddSummary.getMax(), CoreMatchers.equalTo(1.0));
        Assert.assertThat(ddSummary.getMin(), CoreMatchers.equalTo(0.0));
    }
}
