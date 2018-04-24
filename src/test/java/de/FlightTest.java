package de;

import de.data.Flight;
import org.junit.Test;

import java.util.ArrayList;

import static de.Utils.versendeEMail;
import static de.Utils.getFlightString;

public class FlightTest {


    @Test
    public void datumTest() throws Exception {
        Flight flight = new Flight();
        flight.setSsd("1524520200");
        flight.setSad("1524559800");
        flight.setAdd("1524520200");
        flight.setAad("1524559800");

        System.out.println(flight.toString());

    }

    @Test
    public void verschickeMail() throws Exception {
        ArrayList<Flight> flights = new ArrayList<>();
        flights.add(getBasicFlug());
        flights.add(getBasicFlug());
        flights.add(getBasicFlug());
        String flightString = getFlightString(flights);
        versendeEMail(flightString);
    }

    private Flight getBasicFlug(){
        Flight flight = new Flight();
        flight.setDelay(250);
        flight.setSad("");
        flight.setAad("");
        flight.setAdd("");
        flight.setAaid("");
        flight.setDaid("");
        flight.setFc("");
        flight.setSsd("");
        flight.setUnicNumber("");
        return flight;
    }
}
