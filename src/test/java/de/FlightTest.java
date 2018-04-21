package de;

import de.data.Flight;
import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static de.Utils.versendeEMail;
import static de.Utils.getFlightString;

public class FlightTest {

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");

    @Test
    public void datumTest() throws Exception {
        long time = 1524213900;

        Date date = new Date();
        date.setTime(time * 1000);

        System.out.println(dateFormat.format(new Date()));
        System.out.println(dateFormat.format(date));

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
