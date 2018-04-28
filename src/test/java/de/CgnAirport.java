package de;

import de.data.Flight;
import de.utils.CgnAirportUtils;
import org.junit.Test;

import java.util.ArrayList;

public class CgnAirport {

    private CgnAirportUtils flightTrackerCgnUtils = new CgnAirportUtils();


    @Test
    public void readMeTest() {
        ArrayList<Flight> cgnAirportDelay = flightTrackerCgnUtils.getCgnAirportDelay(10);

        for (Flight flight : cgnAirportDelay) {
            System.out.println(flightTrackerCgnUtils.getFlightStringCgnAirport(flight));
        }
    }

}
