package ch5;

import org.assertj.core.api.Assertions;
import org.junit.Assert;
import org.junit.Test;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.*;
import java.util.Date;

public class TimeZoneTest {

    @Test
    public void findTimeZone() {
        ZoneId.getAvailableZoneIds().stream()
                .filter(z -> z.toLowerCase().contains("mexico"))
                .sorted().forEach(System.out::println);
    }

    @Test(expected = DateTimeException.class)
    public void invalidLocalDate() {
        try {
            LocalDate.of(1999, 11, 33);
        } catch (DateTimeException d) {
            d.printStackTrace();
            throw d;
        }
    }

    @Test
    public void getEpochs() {
        LocalDate.now().toEpochDay();
        LocalDateTime.now().toEpochSecond(ZoneOffset.UTC);
        ZonedDateTime.now().toEpochSecond();
    }

    @Test
    public void periods() {
        Period.ofDays(1);
        Assertions.assertThat(Period.ofDays(365)).isNotEqualTo(Period.ofYears(1));
        Assertions.assertThat(Period.ofMonths(12)).isNotEqualTo(Period.ofYears(1));
        Assertions.assertThat(annoDomini().plus(Period.ofMonths(12)))
                .isEqualTo(LocalDate.of(2,1,1))
                .isEqualTo(annoDomini().plus(Period.ofYears(1)));
        Assertions.assertThat(LocalDate.of(1, 12,31)).isEqualTo(annoDomini().plus(Period.ofWeeks(52)));
        Assertions.assertThat(Period.from(Period.ofDays(7))).isEqualTo(Period.ofDays(7));
        Assertions.assertThat(Period.between(LocalDate.of(1999, 12,31),
                LocalDate.of(2000, 1,1))).isEqualTo(Period.ofDays(1));
        try {
            Assertions.assertThat(Period.from(Duration.ofDays(7))).isEqualTo(Period.ofDays(7));
        } catch (DateTimeException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void durations() {
        Duration duration1SecondInNanos = Duration.ofNanos(1_000_000_000);
        Duration duration1SecondInMilis = Duration.ofMillis(1_000);
        Duration duration1Second = Duration.ofSeconds(1);
        Assertions.assertThat(duration1SecondInNanos)
                .isEqualTo(duration1SecondInMilis)
                .isEqualTo(duration1Second);
        LocalDateTime dateTimeAnnoDomini = LocalDateTime.of(annoDomini(), LocalTime.of(0, 0));
        Assertions.assertThat(dateTimeAnnoDomini.plus(duration1Second))
                .isEqualTo(dateTimeAnnoDomini.plus(duration1SecondInMilis))
                .isEqualTo(dateTimeAnnoDomini.plus(duration1SecondInNanos));
        Assertions.assertThat(Duration.ofHours(1)).isEqualTo(Duration.ofMinutes(60));
        Assertions.assertThat(Duration.ofSeconds(60)).isEqualTo(Duration.ofMinutes(1));
        Assertions.assertThat(Duration.ofSeconds(60*60)).isEqualTo(Duration.ofHours(1));
        Duration.parse("PT1H1M1S");

        try {
            Duration dayDuration = Duration.between(LocalDate.of(2019, 7, 3),
                    LocalDate.of(2019, 7, 4));
        } catch (UnsupportedTemporalTypeException e) {
            e.printStackTrace();
            Duration dayDuration = Duration.between(LocalDateTime.of(2019, 7, 3, 0, 0),
                    LocalDateTime.of(2019, 7, 4, 0, 0));
            try {
                LocalDate.now().plus(dayDuration);
            } catch (UnsupportedTemporalTypeException e1) {
                LocalDateTime.now().plus(dayDuration);
                return;
            }
        }

        Assert.fail();
    }

    @Test
    public void chronoUnits() {
        LocalDate _1enero1990 = LocalDate.of(1990, 1, 1);
        LocalDate _2Enero1990 = LocalDate.of(1990, 1, 2);
        Assertions.assertThat(ChronoUnit.DAYS.between(
                LocalDateTime.of(_1enero1990, LocalTime.of(1, 2, 3)),
                LocalDateTime.of(_2Enero1990, LocalTime.of(10, 2, 4))))
                .isEqualTo(1);
        Assertions.assertThat(ChronoUnit.DAYS.between(
                LocalDateTime.of(_1enero1990, LocalTime.of(1, 2, 3, 1)),
                LocalDateTime.of(_2Enero1990, LocalTime.of(1, 2, 3))))
                .isEqualTo(0);

        Assertions.assertThat(ChronoUnit.DAYS.between(
                LocalDateTime.of(_1enero1990, LocalTime.of(1, 2, 3, 1)),
                LocalDateTime.of(_2Enero1990, LocalTime.of(1, 2, 3, 1))))
                .isEqualTo(1);
    }

    @Test
    public void instant() throws InterruptedException {
        Instant start = Instant.now();
        Thread.sleep(100);
        Instant end = Instant.now();

        long seconds = Duration.between(start, end).getNano();
        Assertions.assertThat(seconds).isGreaterThanOrEqualTo(1000_000);

        ZonedDateTime now = ZonedDateTime.now();
        Instant instant = now.toInstant();
        Assertions.assertThat(instant.toEpochMilli()).isGreaterThan(now.toEpochSecond() * 1000);
        Assertions.assertThat(instant.atZone(now.getZone()))
                .isEqualTo(now);

        LocalDateTime.now().toInstant(ZoneOffset.MAX);

        instant.plus(1, ChronoUnit.DAYS);
        try {
            instant.plus(1, ChronoUnit.MONTHS);
        } catch (UnsupportedTemporalTypeException e) {
            e.printStackTrace();

            LocalDate localDateWithInstant = new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            LocalDate localDateWithMilis = LocalDate.ofEpochDay(new Date().getTime() / (1000 * 60 * 60 * 24));

            Assertions.assertThat(localDateWithInstant).isEqualTo(localDateWithMilis);

            return;
        }

        Assert.fail();

    }

    @Test
    public void daylightSavings() {
        LocalDate date = LocalDate.of(2019, Month.APRIL, 7);
        LocalTime time = LocalTime.of(2, 30);
        ZoneId zone = ZoneId.of("Mexico/General");
        ZonedDateTime dateTime = ZonedDateTime.of(date, time, zone);
        System.out.println(dateTime);  // 2016–03–13T03:30–04:00[US/Eastern]
        Assertions.assertThat(dateTime.getHour()).isEqualTo(3);
        int hours = dateTime.getOffset().getTotalSeconds() / 3600;
        Assertions.assertThat(hours).isEqualTo(-5);
        dateTime = dateTime.minusHours(1);
        Assertions.assertThat(dateTime.getHour()).isEqualTo(1);
        hours = dateTime.getOffset().getTotalSeconds() / 3600;
        Assertions.assertThat(hours).isEqualTo(-6);
    }

    private LocalDate annoDomini() {
        return LocalDate.of(1,1,1);
    }
}
