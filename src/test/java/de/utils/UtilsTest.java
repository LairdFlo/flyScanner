package de.utils;

import org.joda.time.DateTime;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class UtilsTest {

    public TestAndProgUtils utils = new TestAndProgUtils();

    @Test
    public void isFutureFlight(){
        DateTime now = new DateTime().plusHours(1);

        boolean positivFlight = utils.isFutureFlight(now);

        assertTrue(positivFlight);
    }

    @Test
    public void isPastFlight(){
        DateTime now = new DateTime().plusHours(-1);

        boolean pastFlight = utils.isFutureFlight(now);

        assertFalse(pastFlight);
    }
}