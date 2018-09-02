package de.utils;

import de.ScheduledTasks;
import de.data.Flight;
import de.data.Quelle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.ArrayList;

public class Utils {

    private static final Logger log = LoggerFactory.getLogger(ScheduledTasks.class);

    private FlightTrackerUtils flightTrackerUtils = new FlightTrackerUtils();
    private CgnAirportUtils flightTrackerCgnUtils = new CgnAirportUtils();

    public String flightTime(ArrayList<Flight> flights) {
        String flightText = "";

        for (Flight flight : flights) {
            if (flight.getQuelle() == Quelle.AIRPORT) {
                flightText += flightTrackerCgnUtils.getFlightStringCgnAirport(flight);
            } else {
                flightText += flightTrackerUtils.getFlightString(flight);
            }
        }
        return flightText;
    }

}
