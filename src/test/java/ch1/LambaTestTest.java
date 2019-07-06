package ch1;

import org.junit.Test;

import java.util.function.Predicate;

import static org.junit.Assert.*;

public class LambaTestTest {
    Predicate p = value -> value == null;

    @Test
    public void name() {
        assertTrue(isNull(null));
        assertFalse(isNull(new Object()));
    }

    private boolean isNull(Object o) {
        return p.test(o);
    }
}