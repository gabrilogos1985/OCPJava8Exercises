package ch3;

import com.sun.xml.internal.ws.util.StringUtils;
import org.junit.Test;

import java.util.Arrays;
import java.util.function.*;
import java.util.stream.Collectors;

public class CollectionsMethodReferencesTest {
    @Test
    public void methodStaticReference() {
        Supplier<Thread> currentThread = Thread::currentThread;
        System.out.println("ThreadNAme: " + currentThread.get().getName());
        Function<String, String> capitalize = StringUtils::capitalize;
        Consumer<String> consumerCApitalize = CollectionsMethodReferencesTest::capitalizeAndPrint;
        printThread(capitalize);
        Arrays.asList("armenta", "BENITO", "Charly").forEach(consumerCApitalize);
    }

    private void printThread(Function<String, String> capitalize) {
        System.out.println(Arrays.asList("a","CAD","Hun").stream().map(capitalize).collect(Collectors.joining()));
    }

    static String capitalizeAndPrint(String letter) {
        String capital = (letter.charAt(0) + "").toUpperCase() + letter.toLowerCase().substring(1);
        System.out.println(String.format("Capital: %s - orig: %s", capital, letter));
        return capital;
    }
}
