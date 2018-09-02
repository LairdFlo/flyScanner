package de;

import de.data.Flight;
import de.utils.CgnAirportUtils;
import de.utils.FlightTrackerUtils;
import de.utils.TestAndProgUtils;
import org.junit.Test;

import java.util.ArrayList;

import static de.config.Configuration.DELAY_TIME;
import static de.utils.MailUtils.versendeEMail;


public class FlightTest {

    private FlightTrackerUtils flightTrackerUtils = new FlightTrackerUtils();
    private CgnAirportUtils flightTrackerCgnUtils = new CgnAirportUtils();
    private TestAndProgUtils testAndProgUtils = new TestAndProgUtils();
    private ArrayList<Flight> cachDelayFlight = new ArrayList();

    @Test
    public void scanTest() {
        readFlights();
        readFlights();
    }

    private void readFlights(){
        boolean delayCgnFlight = addFlight(flightTrackerCgnUtils.getCgnAirportDelay(DELAY_TIME));
        boolean delayFlight = addFlight(flightTrackerUtils.getDelayFlight(DELAY_TIME));

        if(delayFlight || delayCgnFlight){
            versendeEMail(testAndProgUtils.flightTime(cachDelayFlight));
        }
    }

    private boolean addFlight(ArrayList<Flight> flight) {
        if(flight == null) return false;

        boolean newDelay = false;
        for (Flight flightNumber : flight) {
            if (cachDelayFlight.contains(flightNumber) == false) {
                cachDelayFlight.add(flightNumber);
                newDelay = true;
            }
        }
        return newDelay;
    }
}
