package de.utils;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class AirlineWhiteBlacklistTest {

    public Utils utils = new Utils();

    @Test
    public void isPositivAirlineUe(){
        String airline = "UE5500";

        boolean positivAirline = utils.isPositivAirline(airline);

        assertTrue(positivAirline);
    }

    @Test
    public void isNegativAirlineOe(){
        String airline = "OE2346";
        boolean negativAirline = utils.isPositivAirline(airline);

        assertFalse(negativAirline);
    }

    @Test
    public void isNegativAirlineFr(){
        String airline = "FR5126";
        boolean negativAirline = utils.isPositivAirline(airline);

        assertFalse(negativAirline);
    }

}
